package com.gdou.teaching.controller.teacher;


import com.gdou.teaching.Enum.CourseStatusEnum;
import com.gdou.teaching.Enum.FileCategoryEnum;
import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.dataobject.HostHolder;
import com.gdou.teaching.dto.AchievementDTO;
import com.gdou.teaching.dto.CourseDTO;
import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.form.CourseForm;
import com.gdou.teaching.form.CourseUpdateStuForm;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.service.*;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.CourseVO;
import com.gdou.teaching.vo.ResultVO;
import com.gdou.teaching.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.controller
 * @ClassName: TeacherCourseController
 * @Author: carrymaniac
 * @Description: 教师端课程controller
 * @Date: 2019/9/20 6:19 下午
 * @Version:
 */
@Slf4j
@RestController
@RequestMapping("/teacher/course")
public class TeacherCourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;
    @Autowired
    private ExperimentService experimentService;
    @Autowired
    private ClassService classService;
    @Autowired
    private AchievementService achievementService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private RecordService recordService;
    @Autowired
    private FileService fileService;


    /**
     * 教师端主页
     * 获取课程列表
     * @return
     */
    @GetMapping("/list")
    public ResultVO list(){
        User user = hostHolder.getUser();
        List<CourseDTO> list;
        //通过ID获取到用户课程数据
        try{
            list = courseService.listCourse(user.getUserId());
        } catch (TeachingException e) {
            log.error("[TeacherCourseController]查询课程, 查询异常" + e.getMessage());
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMsg());
        }
        //拿到了数据，进行分割，将数据分为"未结束"和"已结束"两部分
        //切割正常课程。
        List<CourseVO> normal = list.stream().filter(c->c.getCourseStatus().intValue()!=(CourseStatusEnum.END.getCode()))
                .map(courseDTO -> {
                    CourseVO courseVO = new CourseVO();
                    BeanUtils.copyProperties(courseDTO, courseVO);
                    return courseVO;
                }).collect(Collectors.toList());
        //切割出过期课程
        List<CourseVO> end = list.stream().filter(c->c.getCourseStatus().intValue()==(CourseStatusEnum.END.getCode())).map(courseDTO -> {
            CourseVO courseVO = new CourseVO();
            BeanUtils.copyProperties(courseDTO, courseVO);
            return courseVO;
        }).collect(Collectors.toList());
        HashMap<String, Object> map = new HashMap<>();
        map.put("normal", normal);
        map.put("end", end);
        return ResultVOUtil.success(map);

    }


    /**
     * 添加课程信息页
     * @param
     * @return
     */
    @GetMapping("/addCourse")
    public ResultVO addCourse() {
        HashMap<String, Object> map = new HashMap<>();
        List<TreeMap> clazzList;
        List<UserDTO> studentList;
        try{
            clazzList = classService.getAllClazzList();
            studentList= userService.getStudentListByClassId(0);
        }catch (TeachingException e){
            log.error("[TeacherCourseController]查询班级列表, 查询异常" + e.getMessage());
            return ResultVOUtil.fail(ResultEnum.SERVER_ERROR.getCode(),ResultEnum.SERVER_ERROR.getMsg());
        }

        HashMap<String,Object> studentForSelect=new HashMap<>();

        for(int i=0;i<clazzList.size();i++){
            TreeMap treeMap = clazzList.get(i);
            HashMap<String,Object> clazzMap=new HashMap<>();
            List<UserVO> userVoList=studentList.stream().filter(c->c.getClassId().equals(treeMap.get("classId"))).map(student->{
                UserVO userVO = new UserVO();
                userVO.setUserId(student.getUserId());
                userVO.setNickname(student.getNickname());
                return userVO;
            }).collect(Collectors.toList());
            clazzMap.put("classId",clazzList.get(i).get("classId"));
            clazzMap.put("className",clazzList.get(i).get("className"));
            clazzMap.put("studentList",userVoList);
            studentForSelect.put("class"+i,clazzMap);
        }
        map.put("studentForSelect", studentForSelect);
        return ResultVOUtil.success(map);
    }

    /**
     * 课程学生管理
     * @param
     * @return
     */
    @GetMapping("/manage/{courseId}")
    public ResultVO manage(@PathVariable(value = "courseId") Integer courseId) {
        HashMap<String, Object> map = new HashMap<>();
        List<TreeMap> clazzList;
        List<UserDTO> studentList;
        List<AchievementDTO> achievementList;
        try{
            clazzList = classService.getAllClazzList();
            studentList= userService.getStudentListByClassId(0);
        }catch (TeachingException e){
            log.error("[TeacherCourseController]查询班级列表, 查询异常" + e.getMessage());
            return ResultVOUtil.fail(ResultEnum.SERVER_ERROR.getCode(),ResultEnum.SERVER_ERROR.getMsg());
        }
        try{
            achievementList= achievementService.getAchievementByCourseId(courseId);
        }catch (TeachingException e){
            log.error("[TeacherCourseController]查询班级列表, 查询异常" + e.getMessage());
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMsg());
        }
        //获取已选课程的学生id列表
        Set<Integer> hadStudentIdSet = achievementList.stream().map(achievementDTO->achievementDTO.getUserId()).collect(Collectors.toSet());
        // 已选学生列表
        List<UserDTO> hadStudentList=studentList.stream().filter(c->hadStudentIdSet.contains(c.getUserId())).map(student->{
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(student.getUserId());
            userDTO.setNickname(student.getNickname());
            userDTO.setClassId(student.getClassId());
            return userDTO;
        }).collect(Collectors.toList());
        // 未选学生列表
        List<UserDTO> ableStudentList=studentList.stream().filter(c->!hadStudentIdSet.contains(c.getUserId())).map(student->{
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(student.getUserId());
            userDTO.setNickname(student.getNickname());
            userDTO.setClassId(student.getClassId());
            return userDTO;
        }).collect(Collectors.toList());
        HashMap<String,Object> studentForSelect=new HashMap<>();
        HashMap<String,Object> hadSelect=new HashMap<>();
        int index=0;
        //根据班级进行分组
        for(TreeMap clazz:clazzList){
            HashMap<String,Object> clazzMap=new HashMap<>();
            List<UserVO> ableStudentVOList=ableStudentList.stream().filter(c->c.getClassId().equals(clazz.get("classId"))).map(student->{
                UserVO userVO = new UserVO();
                userVO.setUserId(student.getUserId());
                userVO.setNickname(student.getNickname());
                return userVO;
            }).collect(Collectors.toList());
            if (!ableStudentVOList.isEmpty()){
                clazzMap.put("classId",clazz.get("classId"));
                clazzMap.put("className",clazz.get("className"));
                clazzMap.put("studentList",ableStudentVOList);
                studentForSelect.put("class"+index,clazzMap);
                index++;
            }
        }
        index=0;
        for(TreeMap clazz:clazzList){
            HashMap<String,Object> clazzMap=new HashMap<>();
            List<UserVO> hadStudentVOList=hadStudentList.stream().filter(c->c.getClassId().equals(clazz.get("classId"))).map(student->{
                UserVO userVO = new UserVO();
                userVO.setUserId(student.getUserId());
                userVO.setNickname(student.getNickname());
                return userVO;
            }).collect(Collectors.toList());
            if (!hadStudentVOList.isEmpty()){
                clazzMap.put("classId",clazz.get("classId"));
                clazzMap.put("className",clazz.get("className"));
                clazzMap.put("studentList",hadStudentVOList);
                hadSelect.put("class"+index,clazzMap);
                index++;
            }
        }
        map.put("studentForSelect", studentForSelect);
        map.put("hadSelect",hadSelect);
        return ResultVOUtil.success(map);
    }

    /**
     * 保存课程信息
     * @param form
     * @param bindingResult
     * @return
     */
    @PostMapping("/save")
    public ResultVO save(@RequestBody @Valid CourseForm form,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("参数格式错误：{}" + form);
            return ResultVOUtil.fail(ResultEnum.BAD_REQUEST.getCode(), ResultEnum.BAD_REQUEST.getMsg());
        }
        CourseDTO courseDTO = new CourseDTO();
        BeanUtils.copyProperties(form, courseDTO);
        if(form.getCourseId()==null){
            courseDTO.setCourseStatus(CourseStatusEnum.NORMAL.getCode().byteValue());
            courseDTO.setCourseNumber(0);
        }
        try {
            courseService.save(courseDTO);
        } catch (TeachingException e) {
            log.error("保存课程,发生异常:{}", e);
            return ResultVOUtil.fail(ResultEnum.SERVER_ERROR.getCode(), ResultEnum.SERVER_ERROR.getMsg());
        }
         // 新增课程课程时执行
        if (form.getCourseId()==null){
            //  todo 异步更新成绩表 addAchievementByClazzId
            List<Integer> studentIdList = form.getAddStudentIdList();
            try{
                achievementService.addAchievementByStudentList(courseDTO.getCourseId(),studentIdList);
                // 异步更新课程及其下属实验的上课人数
                courseService.updateNumber(courseDTO.getCourseId());
            }catch (TeachingException e){
                log.error("更新成绩表,发生异常:{}", e);
                return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(),e.getMessage());
            }
        }
        return ResultVOUtil.success();
    }

    @PostMapping("/updateNumber")
    //课程详情
    public ResultVO updateNumber(@RequestBody @Valid CourseUpdateStuForm form,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("参数不正确：{}" + form);
            throw new TeachingException(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMsg());
        }
        try {
            //修改
            if (form.getAddStudentIdList()!=null && !form.getAddStudentIdList().isEmpty()){
                achievementService.addAchievementByStudentList(form.getCourseId(), form.getAddStudentIdList());
            }
            if (form.getDeleteStudentIdList()!=null &&!form.getDeleteStudentIdList().isEmpty()){
                achievementService.deleteAchievementByStudentList(form.getCourseId(), form.getDeleteStudentIdList());
            }
            //todo  更新上课及其下属实验的人数
            courseService.updateNumber(form.getCourseId());
        } catch (TeachingException e) {
            log.error("更新课程人数,发生异常:{}", e);
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(),e.getMessage());
        }
        return ResultVOUtil.success();
    }

    @GetMapping("/detail/{courseId}")
    public ResultVO<CourseVO> detail(@PathVariable(value = "courseId") Integer courseId){
        CourseVO courseVO = new CourseVO();
        CourseDTO detail;
        try {
            detail=courseService.detail(courseId);
        }catch (TeachingException e){
            log.error("查询课程详细,发生异常:{}", e);
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMsg());
        }
        BeanUtils.copyProperties(detail,courseVO);
        return ResultVOUtil.success(courseVO);
    }

    @GetMapping("/resource/{courseId}")
    public ResultVO resource(@PathVariable(value = "courseId") Integer courseId, @RequestParam(required = false)String keyword){
        if(StringUtils.isEmpty(keyword)){
            //通过课程ID获取课程关联的文件
            return ResultVOUtil.success(
                    fileService.selectFileByCategoryAndFileCategoryId(FileCategoryEnum.COURSE_FILE.getCode(), courseId));
        }else {
            //通过关键字和课程ID获取关联的文件
            return ResultVOUtil.success(
                    fileService.selectFileByCategoryAndFileCategoryIdAndKeyword(FileCategoryEnum.COURSE_FILE.getCode(),courseId,keyword)
            );
        }
    }

    @GetMapping("/invalid/{courseId}")
    public ResultVO invalid(@PathVariable("courseId") Integer courseId) {
        try {
            courseService.invalid(courseId);
        } catch (TeachingException e) {
            log.error("注销课程,发生异常");
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMsg());
        }
        return ResultVOUtil.success();
    }

    @GetMapping("/unlock/{courseId}")
    public ResultVO unlock(@PathVariable("courseId") Integer courseId) {
        try {
            courseService.unlock(courseId);
        } catch (TeachingException e) {
            log.error("恢复课程,发生异常");
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMsg());
        }
        return ResultVOUtil.success();
    }
    @GetMapping("/lock/{courseId}")
    public ResultVO lock(@PathVariable("courseId") Integer courseId) {
        try {
            courseService.lock(courseId);
        } catch (TeachingException e) {
            log.error("锁定课程,发生异常");
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMsg());
        }
        return ResultVOUtil.success();
    }
    @GetMapping("/end/{courseId}")
    public ResultVO end(@PathVariable("courseId") Integer courseId) {
        try {
            courseService.end(courseId);
        } catch (TeachingException e) {
            log.error("完结课程,发生异常");
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMsg());
        }
        return ResultVOUtil.success();
    }
}
