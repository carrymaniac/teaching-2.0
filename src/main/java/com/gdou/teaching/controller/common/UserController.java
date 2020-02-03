package com.gdou.teaching.controller.common;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.Enum.UserIdentEnum;
import com.gdou.teaching.Enum.UserStatusEnum;
import com.gdou.teaching.constant.CookieConstant;
import com.gdou.teaching.constant.RedisConstant;
import com.gdou.teaching.dataobject.HostHolder;
import com.gdou.teaching.dto.CourseDTO;
import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.service.CourseService;
import com.gdou.teaching.service.UserService;
import com.gdou.teaching.util.CookieUtil;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ResultVO;
import com.gdou.teaching.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author carrymaniac
 * @date Created in 16:24 2019-08-09
 * @description 用户控制器
 **/
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final StringRedisTemplate stringRedisTemplate;
    private final UserService userService;
    private final HostHolder hostHolder;

    @Autowired
    public UserController(StringRedisTemplate stringRedisTemplate, UserService userService, HostHolder hostHolder) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.userService = userService;
        this.hostHolder = hostHolder;
    }

    /**
     * 登陆操作 参数校验在controller完成
     *
     * @param userNumber
     * @param password
     * @param response
     * @return
     */
    @PostMapping("/loginForTest")
    @ResponseBody
    public ResultVO Login(@RequestParam("userNumber") String userNumber,
                          @RequestParam("password") String password,
                          @RequestParam("ident") Integer ident,
                          HttpServletResponse response
    ) {
        if (StringUtils.isEmpty(userNumber) || StringUtils.isEmpty(password)) {
            return ResultVOUtil.fail(ResultEnum.BAD_REQUEST);
        }
        try {
            User user = userService.login(userNumber, password);
            //判断身份
            if (user.getUserIdent().intValue() == ident) {
                addToken(user, response);
                HashMap map = new HashMap(3);
                map.put("userId", user.getUserId());
                map.put("nickname", user.getNickname());
                map.put("headUrl", user.getHeadUrl());
                return ResultVOUtil.success(map);
            } else {
                return ResultVOUtil.fail(ResultEnum.USER_NOT_EXIST);
            }
        } catch (TeachingException e) {
            return ResultVOUtil.fail(e);
        }
    }

    /**
     * 登出
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(path = {"/logout"}, method = RequestMethod.GET)
    @ResponseBody
    public ResultVO logout(HttpServletRequest request, HttpServletResponse response) {
        cleanToken(request, response);
        log.info("注销成功");
        return ResultVOUtil.success();
    }


    /**
     * 用户个人信息接口
     *
     * @param userId
     * @return
     */
    @GetMapping(path = {"/info/{id}"})
    @ResponseBody
    public ResultVO<UserDTO> detail(@PathVariable(value = "id") Integer userId) {
        try {
            UserDTO userByUserId = userService.getUserDetailByUserId(userId);
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(userByUserId, userVO);
            if (userByUserId.getUserIdent().equals(UserIdentEnum.SUTUDENT.getCode().byteValue())) {
                userVO.setUserIdent("学生");
            } else if (userByUserId.getUserIdent().equals(UserIdentEnum.TEACHER.getCode().byteValue())) {
                userVO.setUserIdent("老师");
            } else {
                userVO.setUserIdent("管理员");
            }
            return ResultVOUtil.success(userVO);
        } catch (TeachingException e) {
            return ResultVOUtil.fail(e);
        }
    }


    @PostMapping("/updatePassWord")
    @ResponseBody
    public ResultVO updatePassWord(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
        User user = hostHolder.getUser();
        Boolean updatePassword = userService.updatePassword(user.getUserId(), oldPassword, newPassword);
        if (updatePassword) {
            return ResultVOUtil.success();
        } else {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR);
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public ResultVO login(@RequestParam("userNumber") String userNumber,
                          @RequestParam("password") String password,
                          @RequestParam("verifyCode") String verifyCode,
                          @RequestParam("ident") Integer ident,
                          HttpSession httpSession,
                          HttpServletResponse response
    ) {
        if (StringUtils.isEmpty(userNumber)) {
            return ResultVOUtil.fail(ResultEnum.PARAM_NULL.getCode(), "学号/工号不能为空");
        }
        if (StringUtils.isEmpty(password)) {
            return ResultVOUtil.fail(ResultEnum.PARAM_NULL.getCode(), "密码不能为空");
        }
        if (StringUtils.isEmpty(verifyCode)) {
            return ResultVOUtil.fail(ResultEnum.PARAM_NULL.getCode(), "验证码不能为空");
        }
        String kaptchaCode = httpSession.getAttribute("verifyCode") + "";
        log.info("用户输入的验证码为:{},实际的验证码为:{}", verifyCode, kaptchaCode);
        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            return ResultVOUtil.fail(ResultEnum.VERIFYCODE_ERROR);
        }
        httpSession.removeAttribute("verifyCode");
        try {
            User user = userService.login(userNumber, password);
            if (user.getUserIdent().intValue() == ident) {
                addToken(user, response);
                log.info("登陆的用户为：{}", hostHolder.getUser());
                HashMap map = new HashMap(3);
                map.put("userId", user.getUserId());
                map.put("nickname", user.getNickname());
                map.put("headUrl", user.getHeadUrl());
                return ResultVOUtil.success(map);
            } else {
                return ResultVOUtil.fail(ResultEnum.USER_NOT_EXIST);
            }
        } catch (TeachingException e) {
            return ResultVOUtil.fail(e);
        }
    }


    private void addToken(User user, HttpServletResponse response) {
        //设置token至redis
        String token = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token), String.valueOf(user.getUserId()), RedisConstant.EXPIRE, TimeUnit.SECONDS);
        //设置token到cookie
        CookieUtil.set(response, CookieConstant.TOKEN, token, RedisConstant.EXPIRE);
    }

    private void cleanToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (cookie != null) {
            //清除Redis
            stringRedisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));
            //清除cookie
            CookieUtil.set(response, CookieConstant.TOKEN, null, 0);
            hostHolder.clear();
        }
        hostHolder.clear();
    }

    @ResponseBody
    @PostMapping("/resetPassword")
//    @Auth(user=UserIdentEnum.ADMIN)
    public ResultVO resetPassword(@RequestParam("userId")Integer userId,@RequestParam("password")String newPassword){
        userService.resetPassword(userId,newPassword);
        return ResultVOUtil.success();
    }

    @ResponseBody
//    @Auth(user=UserIdentEnum.ADMIN)
    @PostMapping("/invalidUser")
    public ResultVO invalidUser(@RequestBody List<Integer> userId){
        Boolean flag = userService.deleteUserByBatch(userId);
        if (flag){
            return ResultVOUtil.success();
        }else{
            return ResultVOUtil.fail(ResultEnum.USER_NOT_EXIST);
        }
    }
}
