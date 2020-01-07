package com.gdou.teaching.controller.teacher;

import com.gdou.teaching.Enum.CourseStatusEnum;
import com.gdou.teaching.Enum.FileCategoryEnum;
import com.gdou.teaching.Enum.RecordStatusEnum;
import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.dataobject.HostHolder;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.dto.*;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.form.BatchJudgeForm;
import com.gdou.teaching.form.CourseForm;
import com.gdou.teaching.form.CourseUpdateStuForm;
import com.gdou.teaching.form.JudgeForm;
import com.gdou.teaching.mbg.model.Achievement;
import com.gdou.teaching.mbg.model.Class;
import com.gdou.teaching.service.*;
import com.gdou.teaching.util.PoiUtil;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.*;
import com.gdou.teaching.web.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author bo
 * @date Created in 16:30 2019/11/29
 * @description
 **/
@RestController
@RequestMapping("/teacher/achievement")
@Slf4j
public class AchievementController {
    @Autowired
    AchievementService achievementService;
    @Autowired
    RecordService recordService;
    @Autowired
    ClassService classService;
    @Autowired
    UserService userService;
    @Autowired
    ExperimentService experimentService;
    @Autowired
    CourseService courseService;
    @Autowired
    PoiUtil poiUtil;

    //todo  根据classId进行筛选,待完成
    @GetMapping("/list/{courseId}")
    @Auth
    public ResultVO<HashMap> list(@PathVariable(value = "courseId") Integer courseId,
                                  @RequestParam(value = "classId", required = false) Integer classId) {
        HashMap<String,Object> map=new HashMap<>();
        //根据courseId查出所有AchievementDTO
        List<AchievementDTO> achievementDTOList = achievementService.getAchievementByCourseId(courseId);
        //班级列表
        Set<Integer> classIdSet=new HashSet<>();
        for(AchievementDTO achievementDTO:achievementDTOList){
            classIdSet.add(achievementDTO.getClassId());
        }
        List<HashMap> classList=new ArrayList<>();
        for(Integer clazzId:classIdSet){
            Class classByClazzId = classService.getClassByClazzId(clazzId);
            HashMap<String,Object> clazzMap=new HashMap<>();
            clazzMap.put("classId",classByClazzId.getClassId());
            clazzMap.put("className",classByClazzId.getClassName());
            classList.add(clazzMap);
        }
        map.put("classList",classList);
        //获取课程下全部的AchievementVO
        List<AchievementVO> achievementVOList =achievementDTOList.stream().map(achievementDTO -> {
            AchievementVO achievementVO = new AchievementVO();
            achievementVO.setCourseAchievement(achievementDTO.getCourseAchievement());
            User userById = userService.getUserById(achievementDTO.getUserId());
            achievementVO.setUserId(userById.getUserId());
            achievementVO.setUserNumber(userById.getUserNumber());
            achievementVO.setNickName(userById.getNickname());
            achievementVO.setClassId(achievementDTO.getClassId());
            return achievementVO;
        }).collect(Collectors.toList());

        //按班级分割AchievementVO
        for(int i=0;i<classList.size();i++){
            HashMap<String,Object> clazzMap=new HashMap<>();
            Integer clazzId=(Integer) classList.get(i).get("classId");
            String clazzName=(String) classList.get(i).get("className");
            List<AchievementVO> achievementVO = achievementVOList.stream().filter(a->a.getClassId().equals(clazzId))
                    .collect(Collectors.toList());
            clazzMap.put("classId",clazzId);
            clazzMap.put("className",clazzName);
            clazzMap.put("userList",achievementVO);
            map.put("class"+i,clazzMap);
        }
        return ResultVOUtil.success(map);
    }

    /**
     *  通过课程ID,用户ID获取课程的分数
     * @return 该课程下的实验分数。
     */
    @GetMapping("/score/{courseId}")
    @Auth
    public ResultVO scoreList(@PathVariable(value = "courseId") Integer courseId,
                              @RequestParam(value = "userId") Integer userId){
        Achievement achievement = achievementService.getAchievementByUserIdAndCourseId(userId, courseId);
        Double score = achievement.getCourseAchievement();
        //通过课程ID和用户ID获取提交记录列表
        List<RecordDTO> recordByUserIdAndCourseId=recordService.getRecordByUserIdAndCourseId(userId, courseId);
        //提取所需要的字段（实验名-实验ID-实验得分）返回
        List<HashMap<String, String>> experiments = recordByUserIdAndCourseId.stream().map(r -> {
            HashMap<String, String> ex = new HashMap<>(3);
            ex.put("experimentName",r.getExperimentName());
            ex.put("experimentId", r.getExperimentId().toString());
            ex.put("experimentAchievement", r.getExperimentAchievement().toString());
            return ex;
        }).collect(Collectors.toList());
        //获取课程成绩信息
        HashMap<String,Object> map = new HashMap<>();
        map.put("experiments",experiments);
        map.put("score",score);
        return ResultVOUtil.success(map);
    }

    @GetMapping("/judge/{courseId}")
    @Auth
    public ResultVO<HashMap> judge(@PathVariable(value = "courseId", required = true) Integer courseId,
                                   @RequestParam(value = "experimentId", required = false) Integer experimentId,
                                   @RequestParam(value = "classId", required = false) Integer classId) {
        HashMap<String,Object> map=new HashMap<>();
        //根据课程id  获取实验列表
        List<ExperimentDTO> experimentDTOList= experimentService.list(courseId);
        //获取实验列表
        List<HashMap> experimentList=new ArrayList<>();
        for(ExperimentDTO experimentDTO:experimentDTOList){
            HashMap<String,Object> experimentMap=new HashMap<>();
            experimentMap.put("experimentId",experimentDTO.getExperimentId());
            experimentMap.put("experimentName",experimentDTO.getExperimentName());
            experimentList.add(experimentMap);
        }
        map.put("experimentList",experimentList);
        //获取实验的提交记录    缺省默认查找实验列表中的第一个实验
        if (experimentId==null&&!experimentList.isEmpty()){
            experimentId=(Integer) experimentList.get(0).get("experimentId");
        }
        List<RecordDTO> recordDTOS  = recordService.getRecordListByExperimentId(experimentId);
        //班级列表
        Set<Integer> classIdSet=recordDTOS.stream().map(recordDTO -> recordDTO.getClassId()).collect(Collectors.toSet());
        //获取学生列表
        List<JudgeVO> judgeVOList= recordDTOS.stream().map(recordDTO -> {
            JudgeVO judgeVO=new JudgeVO();
            User user = userService.getUserById(recordDTO.getUserId());
            judgeVO.setClassId(user.getClassId());
            judgeVO.setUserId(user.getUserId());
            judgeVO.setUserNumber(user.getUserNumber());
            judgeVO.setNickName(user.getNickname());
            judgeVO.setUserExperimentId(recordDTO.getUserExperimentId());
            judgeVO.setStatus(recordDTO.getStatus().intValue());
            return judgeVO;
        }).collect(Collectors.toList());

        //获取班级列表
        List<HashMap> classList=new ArrayList<>();
        for(Integer clazzId:classIdSet){
            Class classByClazzId = classService.getClassByClazzId(clazzId);
            HashMap<String,Object> clazzMap=new HashMap<>();
            clazzMap.put("classId",classByClazzId.getClassId());
            clazzMap.put("className",classByClazzId.getClassName());
            classList.add(clazzMap);
        }
        map.put("classList",classList);

        //按班级分割
        for(int i=0;i<classList.size();i++){
            //根据班级id筛选结果
            if (classList.get(i).get("classId").equals(classId)){
                HashMap<String,Object> clazzMap=new HashMap<>();
                List<JudgeVO> judgeVO = judgeVOList.stream().filter(a->a.getClassId().equals(classId))
                        .collect(Collectors.toList());
                clazzMap.put("classId",classId);
                clazzMap.put("className",classList.get(i).get("className"));
                clazzMap.put("userList",judgeVO);
                map.put("class0",clazzMap);
                break;
            }
            HashMap<String,Object> clazzMap=new HashMap<>();
            Integer clazzId=(Integer) classList.get(i).get("classId");
            String clazzName=(String) classList.get(i).get("className");
            List<JudgeVO> judgeVO = judgeVOList.stream().filter(a->a.getClassId().equals(clazzId) )
                    .collect(Collectors.toList());
            clazzMap.put("classId",clazzId);
            clazzMap.put("className",clazzName);
            clazzMap.put("userList",judgeVO);
            map.put("class"+i,clazzMap);
        }
        return ResultVOUtil.success(map);
    }

    @GetMapping("/judge/detail/{experimentId}")
    @Auth
    public ResultVO<RecordVO> detail(@PathVariable(value = "experimentId") Integer experimentId,
                                     @RequestParam(value = "userId") Integer userId) {
        RecordVO recordVO=new RecordVO();
        RecordDTO recordDTO = recordService.selectOne(experimentId, userId);
        BeanUtils.copyProperties(recordDTO,recordVO);
        User user = userService.getUserById(recordVO.getUserId());
        recordVO.setUserName(user.getNickname());
        recordVO.setUserNumber(user.getUserNumber());

        return ResultVOUtil.success(recordVO);
    }

    @PostMapping("/judge/save")
    @Auth
    public ResultVO save(@RequestBody @Valid JudgeForm form,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("参数格式错误：{}" + form);
            return ResultVOUtil.fail(ResultEnum.BAD_REQUEST.getCode(), ResultEnum.BAD_REQUEST.getMsg());
        }
        RecordDTO recordDTO = recordService.selectOne(form.getUserExperimentId());
        //判断是否通过审核
        if (form.getStatus().equals(RecordStatusEnum.PASS.getCode())){
            recordDTO.setStatus(RecordStatusEnum.PASS.getCode().byteValue());
            recordDTO.setExperimentAchievement(form.getExperimentAchievement());
        }else{
            recordDTO.setStatus(RecordStatusEnum.NOT_PASS.getCode().byteValue());
            recordDTO.setExperimentAchievement(0d);
        }
        recordDTO.setTeacherComment(form.getTeacherComment());

        recordService.judge(recordDTO);
        //更新Achievement总成绩
        ExperimentDTO detail = experimentService.detail(recordDTO.getExperimentId());
        achievementService.updateAchievement(detail.getCourseId(),recordDTO.getUserId());
        return ResultVOUtil.success();
    }

    /**
     * 批量修改页 所需信息
     * @param experimentId
     * @param classId
     * @return
     */
    @PostMapping("/judge/batch/{experimentId}")
    @Auth
    public ResultVO<HashMap> batch(@PathVariable(value = "experimentId") Integer experimentId,
                                   @RequestParam(value = "classId", required = false) Integer classId) {
        HashMap<String,Object> map=new HashMap<>();

        //获取实验信息
        ExperimentDTO experimentDTO  = experimentService.detail(experimentId);
        map.put("experimentId",experimentDTO.getExperimentId());
        map.put("experimentName",experimentDTO.getExperimentName());
        map.put("time",experimentDTO.getCreateTime());
        map.put("commitNum",experimentDTO.getExperimentCommitNum());

        List<RecordDTO> recordDTOList = recordService.getRecordListByExperimentId(experimentDTO.getExperimentId());
        List<RecordDTO> recordList= recordDTOList.stream().filter(c->c.getStatus().intValue()==RecordStatusEnum.REVIEWING.getCode()).collect(Collectors.toList());
        //获取班级列表
        Set<Integer> classIdSet = recordList.stream().map(c -> c.getClassId()).collect(Collectors.toSet());
        List<HashMap> classList=new ArrayList<>();
        for(Integer clazzId:classIdSet){
            Class classByClazzId = classService.getClassByClazzId(clazzId);
            HashMap<String,Object> clazzMap=new HashMap<>();
            clazzMap.put("classId",classByClazzId.getClassId());
            clazzMap.put("className",classByClazzId.getClassName());
            classList.add(clazzMap);
        }
        map.put("classList",classList);
        //获取学生列表
        List<JudgeVO> judgeVOList;

        judgeVOList = recordList.stream().map(record -> {
            JudgeVO judgeVO=new JudgeVO();
            User user = userService.getUserById(record.getUserId());
            judgeVO.setClassId(user.getClassId());
            judgeVO.setUserId(user.getUserId());
            judgeVO.setUserNumber(user.getUserNumber());
            judgeVO.setNickName(user.getNickname());
            judgeVO.setStatus(record.getStatus().intValue());
            judgeVO.setUserExperimentId(record.getUserExperimentId());
            return judgeVO;
        }).collect(Collectors.toList());
        //按班级分割
        for(int i=0;i<classList.size();i++){
            //根据班级id筛选结果
            HashMap<String,Object> clazzMap=new HashMap<>();
            Integer clazzId=(Integer) classList.get(i).get("classId");
            String clazzName=(String) classList.get(i).get("className");
            List<JudgeVO> judgeVO = judgeVOList.stream().filter(a->a.getClassId().equals(clazzId))
                    .map(judgeVO1 -> {judgeVO1.setClassName(clazzName);return judgeVO1;})
                    .collect(Collectors.toList());
            clazzMap.put("classId",clazzId);
            clazzMap.put("className",clazzName);
            clazzMap.put("userList",judgeVO);
            map.put("class"+i,clazzMap);
        }
        return ResultVOUtil.success(map);
    }


    @PostMapping("/judge/batch/save")
    @Auth
    public ResultVO batchSave(@RequestBody @Valid BatchJudgeForm form,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("参数格式错误：{}" + form);
            return ResultVOUtil.fail(ResultEnum.BAD_REQUEST.getCode(), ResultEnum.BAD_REQUEST.getMsg());
        }
        List<RecordDTO> recordDTOList;
        recordDTOList = form.getUserExperimentIdList().stream().map(userExperimentId -> {
            RecordDTO recordDTO = recordService.selectOne(userExperimentId);
            //判断是否通过审核
            if (form.getStatus().equals(RecordStatusEnum.PASS.getCode())) {
                recordDTO.setStatus(RecordStatusEnum.PASS.getCode().byteValue());
                recordDTO.setExperimentAchievement(form.getExperimentAchievement());
            } else {
                recordDTO.setStatus(RecordStatusEnum.NOT_PASS.getCode().byteValue());
                recordDTO.setExperimentAchievement(0d);
            }
            recordDTO.setTeacherComment(form.getTeacherComment());
            return recordDTO;
        }).collect(Collectors.toList());

        recordService.batchJudge(recordDTOList);
        //todo 异步更新Achievement总成绩
        for(RecordDTO recordDTO:recordDTOList){
            ExperimentDTO detail = experimentService.detail(recordDTO.getExperimentId());
            achievementService.updateAchievement(detail.getCourseId(),recordDTO.getUserId());
        }
        return ResultVOUtil.success();
    }

    /**
     * 导出学生成绩
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping("/export/{courseId}")
    @Auth
    public String export(HttpServletResponse response,@PathVariable(value = "courseId") Integer courseId) throws IOException {
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        CourseDTO detail = courseService.detail(courseId);
        response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(detail.getCourseName()+"成绩表.xls","UTF-8"));
        OutputStream outputStream = response.getOutputStream();
        List<List<String>> collect = achievementService.exportAchievement(detail);
        List<ExperimentDTO> experimentDTOList = experimentService.list(courseId);
        List<String> experimentNameList =  experimentDTOList.stream().map(experimentDTO -> {
            return experimentDTO.getExperimentName();
        }).collect(Collectors.toList());
        List<String> colunNames=new ArrayList<>();
        colunNames.add("班级");
        colunNames.add("学号");
        colunNames.add("姓名");
        colunNames.add("总成绩");
        colunNames.addAll(experimentNameList);
        Workbook sheet = poiUtil.createSheet(detail.getCourseName()+"成绩表",colunNames,collect);
        sheet.write(outputStream);
        System.out.println("success");
        return null;
    }
}
