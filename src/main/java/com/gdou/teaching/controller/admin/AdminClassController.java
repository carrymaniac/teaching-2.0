package com.gdou.teaching.controller.admin;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.Enum.UserIdentEnum;
import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.form.ClazzRegisterForm;
import com.gdou.teaching.mbg.model.Class;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.service.AchievementService;
import com.gdou.teaching.service.ClassService;
import com.gdou.teaching.service.CourseService;
import com.gdou.teaching.service.UserService;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ResultVO;
import com.gdou.teaching.web.Auth;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.controller.admin
 * @ClassName: ClassController
 * @Author: carrymaniac
 * @Description: 班级管理控制器
 * @Date: 2019/12/30 2:04 下午
 * @Version:
 */
@Controller
@RequestMapping("admin/class")
@Slf4j
@Auth(user=UserIdentEnum.ADMIN)
public class AdminClassController {

    private final UserService userService;

    private final AchievementService achievementService;

    private final CourseService courseService;

    private final ClassService classService;

    public AdminClassController(UserService userService, AchievementService achievementService, CourseService courseService, ClassService classService) {
        this.userService = userService;
        this.achievementService = achievementService;
        this.courseService = courseService;
        this.classService = classService;
    }

    @ResponseBody
    @GetMapping("/list")
    public ResultVO list(@RequestParam(value = "page", required = false,defaultValue = "1")Integer page,
                         @RequestParam(value = "size", required = false,defaultValue = "10")Integer size,
                         @RequestParam(value = "classId",required = false,defaultValue = "0")Integer classId,
                         @RequestParam(value = "keyword",required = false,defaultValue = "")String keyword
    ){
        HashMap<String,Object> map = new HashMap<>(3);
        PageInfo pageInfo = userService.getUserListByClassIdAndKeywordAndIdentInPage(classId, page, size,keyword,UserIdentEnum.SUTUDENT.getCode());
        long total = pageInfo.getTotal();
        List<User> list = pageInfo.getList();
        map.put("total",total);
        HashMap<Integer,String> classMap=new HashMap<>();
        if(list!=null&&!list.isEmpty()){
            List<HashMap<String, Object>> userDTOS = list.stream().map(user -> {
                HashMap<String, Object> userDTO = new HashMap(4);
                userDTO.put("userId", user.getUserId());
                userDTO.put("userNumber", user.getUserNumber());
                userDTO.put("nickname", user.getNickname());
                userDTO.put("userStatus", user.getUserStatus());
                Integer clazzId = user.getClassId();
                if (classMap.containsKey(clazzId)){
                    userDTO.put("className", classMap.get(clazzId));
                }else{
                    classMap.put(clazzId, classService.selectOne(clazzId).getClassName());
                    userDTO.put("className", classMap.get(clazzId));
                }
                return userDTO;
            }).collect(Collectors.toList());
            map.put("list",userDTOS);
        }else {
            map.put("list",new ArrayList<>(0));
        }
        if(classId==0){
            //查询一下班级列表供用户前端使用
            List<TreeMap> allClazzList = classService.getAllClazzList();
            map.put("classList",allClazzList);
        }
        return ResultVOUtil.success(map);
    }

    @ResponseBody
    @GetMapping("/info/{userId}")
    public ResultVO info(
            @PathVariable("userId")Integer userId,
            @RequestParam(value = "page", required = false,defaultValue = "1")Integer page,
            @RequestParam(value = "size", required = false,defaultValue = "10")Integer size,
            @RequestParam(value = "keyword",required = false,defaultValue = "")String keyword,
            @RequestParam(value = "status",required = false)Integer status
    ) {
        if (userId == null) {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR);
        }
        UserDTO userById = userService.selectOne(userId);
        if(userById.getUserIdent()==UserIdentEnum.SUTUDENT.getCode().byteValue()){
            UserDTO userInfo = userService.getUserDetailByUserId(userId);
            //查询用户的各课程成绩
            HashMap<String, Object> map = courseService.listCourseForAdminByStudentIdAndKeywordAndStatusInPage(page, size, userId, keyword, status);
            map.put("user",userInfo);
            return ResultVOUtil.success(map);
        }else {
            return ResultVOUtil.fail(203,"查询的用户并非学生");
        }
    }

    @ResponseBody
    @PostMapping("/addStudentByBatch")
    public ResultVO addStudentByBatch(@RequestBody @Valid ClazzRegisterForm form,
                                      BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("参数格式错误：{}" + form);
            return ResultVOUtil.fail(ResultEnum.BAD_REQUEST.getCode(), ResultEnum.BAD_REQUEST.getMsg());
        }
        List<User> userList = form.getUserList();
        //注册班级
        Class registerClass = classService.registerClass(form.getClassName(),userList.size());
        userList.forEach(user->{
            user.setClassId(registerClass.getClassId());
            user.setUserIdent(UserIdentEnum.SUTUDENT.getCode().byteValue());
        });
        userService.addUserByBatch(userList);
        List<Integer> userIdList = userList.stream().map(user -> user.getUserId()).collect(Collectors.toList());
        userService.addUserInfoByBatch(userIdList,form.getCollege(),form.getSeries(),form.getMajor());
        return ResultVOUtil.success();
    }
}
