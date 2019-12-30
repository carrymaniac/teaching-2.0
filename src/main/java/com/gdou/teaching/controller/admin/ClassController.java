package com.gdou.teaching.controller.admin;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.mbg.model.Class;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.service.ClassService;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
public class ClassController {

    @Autowired
    ClassService classService;

    @ResponseBody
    @GetMapping("/list")
    public ResultVO list(@RequestParam(value = "page", required = false,defaultValue = "1")Integer page, @RequestParam(value = "size", required = false,defaultValue = "10")Integer size){
        List<Class> classesByPage = classService.getClassesByPage(page, size);
        return ResultVOUtil.success(classesByPage);
    }

    @ResponseBody
    @GetMapping("/info/{classId}")
    public ResultVO info(@PathVariable("classId")Integer classId){
        if(classId==null){
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR);
        }
        List<User> studentByClazzId = classService.getStudentByClazzId(classId);
        List<UserDTO> userDTOList = studentByClazzId.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        }).collect(Collectors.toList());
        Class classByClazzId = classService.getClassByClazzId(classId);
        HashMap<String,Object> map = new HashMap<>();
        map.put("class",classByClazzId);
        map.put("studentList",userDTOList);
        return ResultVOUtil.success(map);
    }
}
