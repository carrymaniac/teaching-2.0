package com.gdou.teaching.controller.common;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.Enum.UserIdentEnum;
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
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CourseService courseService;
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
                          HttpServletResponse response
    ) {
        if (StringUtils.isEmpty(userNumber)||StringUtils.isEmpty(password)) {
            return ResultVOUtil.fail(ResultEnum.BAD_REQUEST);
        }
        try {
            User user = userService.login(userNumber, password);
            addToken(user, response);
            HashMap map = new HashMap();
            map.put("userId",user.getUserId());
            map.put("nickname",user.getNickname());
            map.put("headUrl",user.getHeadUrl());
            map.put("userId",user.getUserId());
            return ResultVOUtil.success(map);
        } catch (TeachingException e) {
            return ResultVOUtil.fail(e);
        }
    }

    /**
     * 登出
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
     * 主页接口
     *
     * @param model
     * @return
     */
    @GetMapping(path = {"/home"})
    public String homePage(Model model) {
        User user = hostHolder.getUser();
        //说明是老师，需要将老师的课程信息、加载到首页
        if (user.getUserIdent().equals(UserIdentEnum.TEACHER.getCode())) {
            List<CourseDTO> coureses = courseService.getCourseByUserId(user.getUserId());
            model.addAttribute("courses", coureses);
        }
        //如果是学生 需要将学生的课程信息，加载到首页
        if (user.getUserIdent().equals(UserIdentEnum.SUTUDENT.getCode())){

        }
        //如果是学生，需要将
        return "home";
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
            BeanUtils.copyProperties(userByUserId,userVO);
            if(userByUserId.getUserIdent().equals(UserIdentEnum.SUTUDENT.getCode())){
                userVO.setUserIdent("学生");
            }else if(userByUserId.getUserIdent().equals(UserIdentEnum.TEACHER.getCode())){
                userVO.setUserIdent("老师");
            }else{
                userVO.setUserIdent("管理员");
            }
            return ResultVOUtil.success(userVO);
        } catch (TeachingException e) {
            return ResultVOUtil.fail(e);
        }
    }


    @PostMapping("/updatePassWord")
    @ResponseBody
    public ResultVO updatePassWord(@RequestParam("oldPassword")String oldPassword, @RequestParam("newPassword")String newPassword){
        User user = hostHolder.getUser();
        Boolean updatePassword = userService.updatePassword(user.getUserId(), oldPassword, newPassword);
        if(updatePassword){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR);
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public ResultVO login(@RequestParam("userNumber") String userNumber,
                          @RequestParam("password") String password,
                          @RequestParam("verifyCode") String verifyCode,
                          HttpSession httpSession,
                          HttpServletResponse response
    ){
        if (StringUtils.isEmpty(userNumber)||StringUtils.isEmpty(verifyCode)||StringUtils.isEmpty(password)) {
            return ResultVOUtil.fail(ResultEnum.PARAM_NULL);
        }
        String kaptchaCode = httpSession.getAttribute("verifyCode") + "";
        log.info("用户输入的验证码为:{},实际的验证码为:{}",verifyCode,kaptchaCode);
        if(StringUtils.isEmpty(kaptchaCode)||!verifyCode.equals(kaptchaCode)){
            return ResultVOUtil.fail(ResultEnum.VERIFYCODE_ERROR);
        }
        httpSession.removeAttribute("verifyCode");
        try {
            User user = userService.login(userNumber, password);
            addToken(user, response);
            log.info("登陆的用户为：{}", hostHolder.getUser());
            HashMap map = new HashMap(3);
            map.put("userId",user.getUserId());
            map.put("nickname",user.getNickname());
            map.put("headUrl",user.getHeadUrl());
            return ResultVOUtil.success(map);
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


//    /**
//     * 注册操作，表单参数校验在controller完成
//     *
//     * @param form
//     * @param bindingResult
//     * @return
//     */
//    @ResponseBody
//    @PostMapping("/register")
//    public ResultVO register(@Valid UserRegisterForm form, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            log.error("用户注册参数不正确：{}" + form);
//            throw new TeachingException(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMsg());
//        }
//        User user = UserRegisterForm2User.convert(form);
//        try {
//            userService.register(user);
//            return ResultVOUtil.success();
//        } catch (TeachingException e) {
//            return ResultVOUtil.fail(e.getCode(), e.getMessage());
//        }
//    }
}
