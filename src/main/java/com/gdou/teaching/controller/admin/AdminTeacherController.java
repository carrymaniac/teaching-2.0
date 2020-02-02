package com.gdou.teaching.controller.admin;

import com.gdou.teaching.Enum.UserIdentEnum;
import com.gdou.teaching.Enum.UserStatusEnum;
import com.gdou.teaching.dto.CourseDTO;
import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.service.CourseService;
import com.gdou.teaching.service.UserService;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ResultVO;
import com.gdou.teaching.web.Auth;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        PageInfo pageInfo = userService.getUserListByClassIdAndKeywordInPage(0,page,size,keyword,UserIdentEnum.TEACHER.getCode(), UserStatusEnum.NORMAL.getCode());
        return ResultVOUtil.success(pageInfo);
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


    @ResponseBody
    @GetMapping("/teacherInfo")
    public ResultVO teacherInfo(@RequestParam("teacherId")Integer teacherId){
        UserDTO userDetailByUserId = userService.getUserDetailByUserId(teacherId);
        List<CourseDTO> courseByUserId = courseService.listCourseForAdminByTeacherId(teacherId);
        HashMap<String,Object> map= new HashMap<>(2);
        map.put("user",userDetailByUserId);
        map.put("courseList",courseByUserId);
        return ResultVOUtil.success(map);
    }

}
