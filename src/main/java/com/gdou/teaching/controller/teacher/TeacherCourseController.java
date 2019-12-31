package com.gdou.teaching.controller.teacher;


import com.gdou.teaching.Enum.CourseStatusEnum;
import com.gdou.teaching.Enum.FileCategoryEnum;
import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.Enum.UserIdentEnum;
import com.gdou.teaching.constant.CommonConstant;
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
import com.gdou.teaching.web.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static com.gdou.teaching.Enum.UserIdentEnum.TEACHER;

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
    private ClassService classService;
    @Autowired
    private AchievementService achievementService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private FileService fileService;


    /**
     * 教师端主页
     * 获取课程列表
     * @return
     */
    @GetMapping("/list")
    @Auth
    public ResultVO list(){
        User user = hostHolder.getUser();
        //通过ID获取到用户课程数据
        List<CourseDTO> list = courseService.listCourseForTeacher(user.getUserId());
        if(list==null){
            return ResultVOUtil.success();
        }
        //拿到了数据，进行分割，将数据分为"未结束"和"已结束"两部分
        Map<Boolean, List<CourseVO>> collect = list.stream().map(courseDTO -> {
            CourseVO courseVO = new CourseVO();
            BeanUtils.copyProperties(courseDTO, courseVO);
            return courseVO;
        }).collect(Collectors.groupingBy(courseVO -> courseVO.getCourseStatus().intValue() == (CourseStatusEnum.END.getCode())));
        //切割正常课程。
        List<CourseVO> normal = collect.get(false);
        //切割出过期课程
        List<CourseVO> end = collect.get(true);
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("normal", normal);
        map.put("end", end);
        return ResultVOUtil.success(map);
    }


    /**
     * 班级下拉框
     * 返回下拉框所需的数据-包括班级、学生等一系列信息
     * @param
     * @return
     */
    @GetMapping("/classSelectList")
    @Auth
    public ResultVO classSelectList() {
        //获取班级列表
        List<TreeMap> clazzList =classService.getAllClazzList();
        //获取所有学生的列表
        List<UserDTO> studentList = userService.getStudentListByClassId(0);
        //分组
        Map<Integer, List<UserDTO>> collect = studentList.stream().collect(Collectors.groupingBy(UserDTO::getClassId));
        List<HashMap> list = new ArrayList<>(clazzList.size());
        for(TreeMap map:clazzList){
            HashMap<String,Object> clazzMap=new HashMap<>(3);
            List<UserDTO> userDTOS = collect.get(map.get("classId"));
            if(userDTOS!=null&&!userDTOS.isEmpty()){
                List<UserVO> studentVoList = collect.get(map.get("classId")).stream().map(student -> {
                    UserVO userVO = new UserVO();
                    userVO.setUserId(student.getUserId());
                    userVO.setNickname(student.getNickname());
                    return userVO;
                }).collect(Collectors.toList());
                clazzMap.put("studentList",studentVoList);
            }
            clazzMap.put("classId",map.get("classId"));
            clazzMap.put("className",map.get("className"));
            list.add(clazzMap);
        }
        return ResultVOUtil.success(list);
    }

    /**
     * 课程学生管理
     * @param
     * @return
     */
    @GetMapping("/manage/{courseId}")
    @Auth
    public ResultVO manage(@PathVariable(value = "courseId") Integer courseId) {
        HashMap<String, Object> map = new HashMap<>();
        //获取所有的班级列表
        List<TreeMap> clazzList = classService.getAllClazzList();
        //获取所有的学生列表
        List<UserDTO> studentList= userService.getStudentListByClassId(0);

        //获取已选课程的学生id列表
        List<AchievementDTO> achievementList = achievementService.getAchievementByCourseId(courseId);
        Set<Integer> hadStudentIdSet = achievementList.stream().map(achievementDTO->achievementDTO.getUserId()).collect(Collectors.toSet());

        //处理学生列表，并将其切割为已选该课和未选该课两部分
        Map<Boolean, List<UserDTO>> collect = studentList.stream().map(student -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(student.getUserId());
            userDTO.setNickname(student.getNickname());
            userDTO.setClassId(student.getClassId());
            return userDTO;
        }).collect(Collectors.groupingBy(c -> hadStudentIdSet.contains(c.getUserId())));
        //获取已经选了该课的学生和未选的学生
        List<UserDTO> hadStudentList = collect.get(true);
        List<UserDTO> ableStudentList = collect.get(false);
        //将学生按班级ID分组
        Map<Integer, List<UserDTO>> ableStudentListGroupByClassId = ableStudentList.stream().collect(Collectors.groupingBy(UserDTO::getClassId));
        Map<Integer, List<UserDTO>> hadStudentListGroupByClassId = hadStudentList.stream().collect(Collectors.groupingBy(UserDTO::getClassId));

        List studentForSelect= new ArrayList();
        List studentHadSelect=new ArrayList();

        for(TreeMap clazz:clazzList){
            HashMap<String,Object> clazzMapforSelect=new HashMap<>(3);
            HashMap<String,Object> clazzMapHad=new HashMap<>(3);
            //获取归属classId下的未选课的学生，并处理为userVO
            List<UserDTO> userDTOListFromAble = ableStudentListGroupByClassId.get(clazz.get("classId"));
            if(userDTOListFromAble!=null&&!userDTOListFromAble.isEmpty()){
                List<UserVO> userVOList = userDTOListFromAble.stream().map(student -> {
                    UserVO userVO = new UserVO();
                    userVO.setUserId(student.getUserId());
                    userVO.setNickname(student.getNickname());
                    return userVO;
                }).collect(Collectors.toList());
                clazzMapforSelect.put("studentList",userVOList);
            }
            clazzMapforSelect.put("classId",clazz.get("classId"));
            clazzMapforSelect.put("className",clazz.get("className"));
            studentForSelect.add(clazzMapforSelect);
            //获取归属classId下的已经选课的学生，并处理为userVO
            List<UserDTO> userDTOListFromHad = hadStudentListGroupByClassId.get(clazz.get("classId"));
            if(userDTOListFromHad!=null&&!userDTOListFromHad.isEmpty()){
                List<UserVO> userVOList = userDTOListFromHad.stream().map(student -> {
                    UserVO userVO = new UserVO();
                    userVO.setUserId(student.getUserId());
                    userVO.setNickname(student.getNickname());
                    return userVO;
                }).collect(Collectors.toList());
                clazzMapHad.put("studentList",userVOList);
            }
            clazzMapHad.put("classId",clazz.get("classId"));
            clazzMapHad.put("className",clazz.get("className"));
            studentHadSelect.add(clazzMapHad);
        }
        map.put("studentForSelect", studentForSelect);
        map.put("studentHadSelect",studentHadSelect);
        return ResultVOUtil.success(map);
    }

    /**
     * 保存课程信息
     * @param form
     * @param bindingResult
     * @return
     */
    @PostMapping("/save")
    @Auth
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
        courseService.save(courseDTO);
         // 新增课程课程时执行
        if (form.getCourseId()==null){
            //  todo 异步更新成绩表 addAchievementByClazzId
            List<Integer> studentIdList = form.getAddStudentIdList();
            if(studentIdList!=null&&!studentIdList.isEmpty()){
                achievementService.addAchievementByStudentList(courseDTO.getCourseId(),studentIdList);
                // 异步更新课程及其下属实验的上课人数
                courseService.updateNumber(courseDTO.getCourseId());
            }
        }
        return ResultVOUtil.success();
    }

    /**
     * 更新课程人员
     * @param form
     * @param bindingResult
     * @return
     */
    @PostMapping("/updateNumber")
    public ResultVO updateNumber(@RequestBody @Valid CourseUpdateStuForm form,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("参数不正确：{}" + form);
            throw new TeachingException(ResultEnum.PARAM_ERROR);
        }
        //检查主表是否存在
        courseService.info(form.getCourseId());
        //修改
        if (form.getAddStudentIdList()!=null && !form.getAddStudentIdList().isEmpty()){
            achievementService.addAchievementByStudentList(form.getCourseId(), form.getAddStudentIdList());
        }
        if (form.getDeleteStudentIdList()!=null &&!form.getDeleteStudentIdList().isEmpty()){
            achievementService.deleteAchievementByStudentList(form.getCourseId(), form.getDeleteStudentIdList());
        }
        //todo  更新上课及其下属实验的人数
        courseService.updateNumber(form.getCourseId());
        return ResultVOUtil.success();
    }

    @GetMapping("/detail/{courseId}")
    @Auth
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
    @Auth
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
    @Auth
    public ResultVO invalid(@PathVariable("courseId") Integer courseId) {
        courseService.invalid(courseId);
        //删除课程下的选课记录
        achievementService.deleteAchievementByCourseId(courseId);
        return ResultVOUtil.success();
    }

    @GetMapping("/unlock/{courseId}")
    @Auth
    public ResultVO unlock(@PathVariable("courseId") Integer courseId) {
        courseService.unlock(courseId);
        return ResultVOUtil.success();
    }
    @GetMapping("/lock/{courseId}")
    @Auth
    public ResultVO lock(@PathVariable("courseId") Integer courseId) {
        courseService.lock(courseId);
        return ResultVOUtil.success();
    }
    @GetMapping("/end/{courseId}")
    @Auth
    public ResultVO end(@PathVariable("courseId") Integer courseId) {
        courseService.end(courseId);
        return ResultVOUtil.success();
    }
}
