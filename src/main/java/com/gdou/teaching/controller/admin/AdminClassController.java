package com.gdou.teaching.controller.admin;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.dto.CourseDTO;
import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.mbg.model.Class;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.service.AchievementService;
import com.gdou.teaching.service.ClassService;
import com.gdou.teaching.service.CourseService;
import com.gdou.teaching.service.UserService;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ResultVO;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

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
public class AdminClassController {

    @Autowired
    UserService userService;

    @Autowired
    AchievementService achievementService;

    @Autowired
    CourseService courseService;

    @Autowired
    ClassService classService;

    @ResponseBody
    @GetMapping("/list")
    public ResultVO list(@RequestParam(value = "page", required = false,defaultValue = "1")Integer page,
                         @RequestParam(value = "size", required = false,defaultValue = "10")Integer size,
                         @RequestParam(value = "classId",required = false,defaultValue = "0")Integer classId,
                         @RequestParam(value = "keyword",required = false,defaultValue = "")String keyword
    ){
        HashMap<String,Object> map = new HashMap<>(2);
        PageInfo pageInfo = userService.getStudentListByClassIdAndKeywordInPage(classId, page, size,keyword);
        map.put("userList",pageInfo);
        if(classId==0){
            //查询一下班级列表供用户前端使用
            List<TreeMap> allClazzList = classService.getAllClazzList();
            map.put("classList",allClazzList);
        }
        return ResultVOUtil.success(map);
    }

    @ResponseBody
    @GetMapping("/info/{userId}")
    public ResultVO info(@PathVariable("userId")Integer userId) {
        if (userId == null) {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR);
        }
        UserDTO userInfo = userService.getUserDetailByUserId(userId);
        //查询用户的各课程成绩
        List<CourseDTO> courseDTOS = courseService.listCourseForAdminByStudentId(userId);
        HashMap map = new HashMap(2);
        map.put("user",userInfo);
        map.put("courseList",courseDTOS);
        return ResultVOUtil.success(map);
    }

}
