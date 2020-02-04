package com.gdou.teaching.controller.admin;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.Enum.UserIdentEnum;
import com.gdou.teaching.Enum.UserStatusEnum;
import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.service.CourseService;
import com.gdou.teaching.service.UserService;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ResultVO;
import com.gdou.teaching.web.Auth;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.controller.admin
 * @ClassName: AdminUserController
 * @Author: carrymaniac
 * @Description: 管理员端用户控制器
 * @Date: 2019/12/30 2:35 下午
 * @Version:
 */
@Controller
@RequestMapping("admin/teacher")
@Auth(user=UserIdentEnum.ADMIN)
public class AdminTeacherController {

    private final UserService userService;
    private final CourseService courseService;

    public AdminTeacherController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @ResponseBody
    @GetMapping("/teacherList")
    public ResultVO teacherList(@RequestParam(value = "page",defaultValue = "1",required = false) Integer page,
                                @RequestParam(value = "size",defaultValue = "10",required = false) Integer size,
                                @RequestParam(value = "keyword",required = false,defaultValue = "")String keyword){
        PageInfo<User> userPageInfo = userService.getUserListByClassIdAndKeywordAndIdentInPage(0,page,size,keyword,UserIdentEnum.TEACHER.getCode());
        long total = userPageInfo.getTotal();
        HashMap map = new HashMap(2);
        map.put("total",total);
        List<User> list = userPageInfo.getList();
        if(list==null||list.isEmpty()){
            map.put("list",new ArrayList<>(0));
        }else {
            List<UserDTO> collect = list.stream().map(teacher -> {
                UserDTO userDTO = new UserDTO();
                userDTO.setUserId(teacher.getUserId());
                userDTO.setUserNumber(teacher.getUserNumber());
                userDTO.setNickname(teacher.getNickname());
                return userDTO;
            }).collect(Collectors.toList());
            map.put("list",collect);
        }
        return ResultVOUtil.success(map);
    }

    @ResponseBody
    @PostMapping("/addTeacher")
    public ResultVO addTeacher(@RequestParam("userNumber")String userNumber,@RequestParam("password") String password,@RequestParam("nickName")String nickName){
        User user = new User();
        user.setUserIdent(UserIdentEnum.TEACHER.getCode().byteValue());
        user.setNickname(nickName);
        user.setUserNumber(userNumber);
        userService.addUser(user);
        return ResultVOUtil.success();
    }

    @ResponseBody
    @PostMapping("/addTeacherByBatch")
    public ResultVO addTeacherByBatch(@RequestBody List<User> userList){
        userList.forEach(user->{
            user.setClassId(0);
            user.setUserIdent(UserIdentEnum.TEACHER.getCode().byteValue());
        });
        userService.addUserByBatch(userList);
        List<Integer> userIdList = userList.stream().map(user -> user.getUserId()).collect(Collectors.toList());
        userService.addUserInfoByBatch(userIdList,"","","");
        return ResultVOUtil.success();
    }


    /**
     *
     * @param teacherId
     * @param page
     * @param size
     * @param status 可选参数，若为0，则返回"进行中"的课程;若为3，则返回"已结束"的课程
     * @param keyword
     * @return
     */
    @ResponseBody
    @GetMapping("/teacherInfo")
    public ResultVO teacherInfo(
            @RequestParam("teacherId")int teacherId,
            @RequestParam(value = "page",defaultValue = "1")int page,
            @RequestParam(value = "size",defaultValue = "5")int size,
            @RequestParam(value = "status",required = false)Integer status,
            @RequestParam(value = "keyword",required = false)String keyword

    ){
        UserDTO userDetailByUserId = userService.getUserDetailByUserId(teacherId);
        HashMap<String, Object> map = courseService.listCourseForAdminByTeacherIdAndKeywordInPage(teacherId, page, size, keyword,status);
        map.put("user",userDetailByUserId);
        return ResultVOUtil.success(map);
    }

    @PostMapping("/banUser")
    @ResponseBody
    public ResultVO banUser(@RequestParam("userId")Integer userId){
        User userById = userService.getUserById(userId);
        if(!userById.getUserIdent().equals(UserIdentEnum.TEACHER.getCode().byteValue())){
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR);
        }
        //检查状态
        if(userById.getUserStatus().equals(UserStatusEnum.NORMAL.getCode().byteValue())){
            //状态正常的用户,进行禁用操作
            userById.setUserStatus(UserStatusEnum.BAN.getCode().byteValue());
            boolean b = userService.updateUserMaster(userById);
            return b?ResultVOUtil.success():ResultVOUtil.fail(ResultEnum.SERVER_ERROR);
        }else {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR);
        }
    }


    @PostMapping("/delUser")
    @ResponseBody
    public ResultVO delUser(@RequestParam("userId")Integer userId){
        User userById = userService.getUserById(userId);
        if(!userById.getUserIdent().equals(UserIdentEnum.TEACHER.getCode().byteValue())){
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR);
        }
        //检查状态
        if(userById.getUserStatus().equals(UserStatusEnum.BAN.getCode().byteValue())){
            //状态正常的用户,进行禁用操作
            userById.setUserStatus(UserStatusEnum.INVALID.getCode().byteValue());
            boolean b = userService.updateUserMaster(userById);
            return b?ResultVOUtil.success():ResultVOUtil.fail(ResultEnum.SERVER_ERROR);
        }else if (userById.getUserStatus().equals(UserStatusEnum.NORMAL.getCode().byteValue())){
            return ResultVOUtil.fail(203,"请先禁用该用户再进行删除");
        }else {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR);
        }
    }

}
