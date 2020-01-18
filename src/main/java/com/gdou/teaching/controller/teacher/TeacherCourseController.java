package com.gdou.teaching.controller.teacher;


import com.gdou.teaching.Enum.CourseStatusEnum;
import com.gdou.teaching.Enum.FileCategoryEnum;
import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.Enum.UserIdentEnum;
import com.gdou.teaching.constant.CommonConstant;
import com.gdou.teaching.dataobject.Evaluation;
import com.gdou.teaching.dataobject.HostHolder;
import com.gdou.teaching.dto.AchievementDTO;
import com.gdou.teaching.dto.CourseDTO;
import com.gdou.teaching.dto.FileDTO;
import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.form.CourseForm;
import com.gdou.teaching.form.CourseInfoUpdateForm;
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
     *  树形菜单形式
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
        List<Evaluation> list = new ArrayList<>(clazzList.size());
        for(TreeMap map:clazzList){
            Evaluation top=new Evaluation();
            List<UserDTO> userDTOS = collect.get(map.get("classId"));
            if(userDTOS!=null&&!userDTOS.isEmpty()){
                List<Evaluation> evaluationList = collect.get(map.get("classId")).stream().map(student -> {
                    Evaluation evaluation=new Evaluation();
                    evaluation.setPid(map.get("classId").toString());
                    evaluation.setId(evaluation.getPid()+"-"+student.getUserId().toString());
                    evaluation.setLabel(student.getNickname());
                    return evaluation;
                }).collect(Collectors.toList());
                top.setChildren(evaluationList);
            }
            top.setId(map.get("classId").toString());
            top.setLabel(map.get("className").toString());
            list.add(top);
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

        Set<Integer>hadStudentIdSet;
        //获取已选课程的学生id列表
        List<AchievementDTO> achievementList = achievementService.getAchievementByCourseId(courseId);
        if (achievementList!=null){
            hadStudentIdSet = achievementList.stream().map(achievementDTO->achievementDTO.getUserId()).collect(Collectors.toSet());
        }else{
            hadStudentIdSet=new HashSet<>();
        }
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
        if(hadStudentList==null){
            hadStudentList=new ArrayList<>();
        }
        //将学生按班级ID分组
        List<UserDTO> ableStudentList = collect.get(false);
        if(ableStudentList==null){
            ableStudentList=new ArrayList<>();
        }
        Map<Integer, List<UserDTO>> ableStudentListGroupByClassId = ableStudentList.stream().collect(Collectors.groupingBy(UserDTO::getClassId));
        Map<Integer, List<UserDTO>> hadStudentListGroupByClassId = hadStudentList.stream().collect(Collectors.groupingBy(UserDTO::getClassId));

        List<Evaluation> studentForSelect= new ArrayList();
        List<Evaluation> studentHadSelect= new ArrayList();

        for(TreeMap clazz:clazzList){
            if(!ableStudentListGroupByClassId.isEmpty()){
                Evaluation clazzMapForSelect=new Evaluation();
                //获取归属classId下的未选课的学生，并处理为userVO
                List<UserDTO> userDTOListFromAble = ableStudentListGroupByClassId.get(clazz.get("classId"));
                if(userDTOListFromAble!=null&&!userDTOListFromAble.isEmpty()){
                    List<Evaluation> userVOList = userDTOListFromAble.stream().map(student -> {
                        Evaluation userVO = new Evaluation();
                        userVO.setPid(clazz.get("classId").toString());
                        userVO.setId(userVO.getPid()+"-"+student.getUserId().toString());
                        userVO.setLabel(student.getNickname());
                        return userVO;
                    }).collect(Collectors.toList());
                    clazzMapForSelect.setChildren(userVOList);
                }
                clazzMapForSelect.setId(clazz.get("classId").toString());
                clazzMapForSelect.setLabel(clazz.get("className").toString());
                studentForSelect.add(clazzMapForSelect);
            }
            if(!hadStudentListGroupByClassId.isEmpty()){
                Evaluation clazzMapHad=new Evaluation();
                //获取归属classId下的已经选课的学生，并处理为userVO
                List<UserDTO> userDTOListFromHad = hadStudentListGroupByClassId.get(clazz.get("classId"));
                if(userDTOListFromHad!=null&&!userDTOListFromHad.isEmpty()){
                    List<Evaluation> userVOList = userDTOListFromHad.stream().map(student -> {
                        Evaluation userVO = new Evaluation();
                        userVO.setPid(clazz.get("classId").toString());
                        userVO.setId(userVO.getPid()+"-"+student.getUserId().toString());
                        userVO.setLabel(student.getNickname());
                        return userVO;
                    }).collect(Collectors.toList());
                    clazzMapHad.setChildren(userVOList);
                }
                clazzMapHad.setId(clazz.get("classId").toString());
                clazzMapHad.setLabel(clazz.get("className").toString());
                studentHadSelect.add(clazzMapHad);
            }
        }
        map.put("studentForSelect", studentForSelect);
        map.put("studentHadSelect",studentHadSelect);
        return ResultVOUtil.success(map);
    }

    /**
     * 新增课程  保存信息
     * @param form
     * @param bindingResult
     * @return
     */
    @PostMapping("/save")
    @Auth
    public ResultVO save(@RequestBody @Valid CourseForm form,
                         BindingResult bindingResult) {
        User user = hostHolder.getUser();
        if (bindingResult.hasErrors()) {
            log.error("参数格式错误：{}" + form);
            return ResultVOUtil.fail(ResultEnum.BAD_REQUEST.getCode(), ResultEnum.BAD_REQUEST.getMsg());
        }
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setTeacherId(user.getUserId());
        BeanUtils.copyProperties(form, courseDTO);
        courseDTO.setCourseStatus(CourseStatusEnum.NORMAL.getCode().byteValue());
        courseDTO.setCourseNumber(0);
        courseService.save(courseDTO);
        //  todo 异步更新成绩表 addAchievementByClazzId
        List<String> addStudentIdList = form.getAddStudentIdList();
        //过滤多余的字符
        List<Integer> studentIdList =addStudentIdList.stream().map(studentId->{
            String[] split = studentId.trim().split("-");
            return Integer.parseInt(split[split.length-1]);
        }).collect(Collectors.toList());
        if(studentIdList!=null&&!studentIdList.isEmpty()){
            achievementService.addAchievementByStudentList(courseDTO.getCourseId(),studentIdList);
            // 异步更新课程及其下属实验的上课人数
            courseService.updateNumber(courseDTO.getCourseId());
        }
        return ResultVOUtil.success();
    }

    /**
     * 更新课程基本信息
     * @param form
     * @param bindingResult
     * @return
     */
    @PostMapping("/updateCourseInfo")
    @Auth
    public ResultVO updateCourseInfo(@RequestBody @Valid CourseInfoUpdateForm form,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("参数格式错误：{}" + form);
            return ResultVOUtil.fail(ResultEnum.BAD_REQUEST.getCode(), ResultEnum.BAD_REQUEST.getMsg());
        }
        CourseDTO courseDTO = new CourseDTO();
        BeanUtils.copyProperties(form, courseDTO);
        courseService.save(courseDTO);
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
        List<String> addStudentIdList = form.getAddStudentIdList();
        List<String> deleteStudentIdList = form.getDeleteStudentIdList();
        //修改
        if (addStudentIdList!=null && !addStudentIdList.isEmpty()){
            //过滤多余的字符
            List<Integer> studentIdList =addStudentIdList.stream().map(studentId->{
                String[] split = studentId.trim().split("-");
                return Integer.parseInt(split[split.length-1]);
            }).collect(Collectors.toList());
            achievementService.addAchievementByStudentList(form.getCourseId(), studentIdList);
        }
        if (deleteStudentIdList!=null &&!deleteStudentIdList.isEmpty()){
            //过滤多余的字符
            List<Integer> studentIdList =deleteStudentIdList.stream().map(studentId->{
                String[] split = studentId.trim().split("-");
                return Integer.parseInt(split[split.length-1]);
            }).collect(Collectors.toList());
            achievementService.deleteAchievementByStudentList(form.getCourseId(), studentIdList);
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
            //通过课程ID获取课程关连的文件
            List<FileDTO> result = fileService.selectFileByCategoryAndFileCategoryId(FileCategoryEnum.COURSE_FILE.getCode(), courseId);
            if(result!=null&&!result.isEmpty()){
                return ResultVOUtil.success(result);
            }
        }else {
            List<FileDTO> result = fileService.selectFileByCategoryAndFileCategoryIdAndKeyword(FileCategoryEnum.COURSE_FILE.getCode(),courseId,keyword);
            //通过关键字和课程ID获取关联的文件
            if(result!=null&&!result.isEmpty()){
                return ResultVOUtil.success(result);
            }
        }
        return ResultVOUtil.success(new ArrayList<>());
    }


    @GetMapping("/invalid/{courseId}")
    @Auth
    public ResultVO invalid(@PathVariable("courseId") Integer courseId) {
        if(courseService.invalid(courseId)){
            //删除课程下的选课记录
            achievementService.deleteAchievementByCourseId(courseId);
        }
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
