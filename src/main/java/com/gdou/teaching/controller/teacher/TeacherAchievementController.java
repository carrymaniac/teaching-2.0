package com.gdou.teaching.controller.teacher;

import com.gdou.teaching.Enum.RecordStatusEnum;
import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.dto.*;
import com.gdou.teaching.form.BatchJudgeForm;
import com.gdou.teaching.form.JudgeForm;
import com.gdou.teaching.mbg.model.Achievement;
import com.gdou.teaching.mbg.model.Class;
import com.gdou.teaching.service.*;
import com.gdou.teaching.service.impl.RedisServiceImpl;
import com.gdou.teaching.util.PoiUtil;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.*;
import com.gdou.teaching.web.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@Auth
public class TeacherAchievementController {
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
    @Autowired
    RedisServiceImpl redisService;

    @GetMapping("/list/{courseId}")
    public ResultVO<HashMap> list(@PathVariable(value = "courseId") Integer courseId) {
        HashMap<String,Object> map=new HashMap<>();
        //根据courseId查出所有AchievementDTO
        List<AchievementDTO> achievementDTOList = achievementService.getAchievementByCourseId(courseId);
        if (achievementDTOList==null||achievementDTOList.isEmpty()){
            return ResultVOUtil.success(map);
        }
        //班级列表
        Set<Integer> classIdSet=new HashSet<>();
        for(AchievementDTO achievementDTO:achievementDTOList){
            classIdSet.add(achievementDTO.getClassId());
        }
        List<HashMap> classList=new ArrayList<>();
        for(Integer clazzId:classIdSet){
            Class classByClazzId = classService.selectOne(clazzId);
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
            UserDTO userById = userService.selectOne(achievementDTO.getUserId());
            achievementVO.setUserId(userById.getUserId());
            achievementVO.setUserNumber(userById.getUserNumber());
            achievementVO.setNickName(userById.getNickname());
            achievementVO.setClassId(achievementDTO.getClassId());
            return achievementVO;
        }).collect(Collectors.toList());

        //按班级分割AchievementVO
        List<HashMap> classData=new ArrayList<>(classList.size());
        for(HashMap clazz:classList) {
            HashMap<String,Object> clazzMap=new HashMap<>();
            Integer clazzId=(Integer) clazz.get("classId");
            String clazzName=(String) clazz.get("className");
            List<AchievementVO> achievementVO = achievementVOList.stream().filter(a->a.getClassId().equals(clazzId))
                    .collect(Collectors.toList());
            clazzMap.put("classId",clazzId);
            clazzMap.put("className",clazzName);
            clazzMap.put("userList",achievementVO);
            classData.add(clazzMap);
        }
        map.put("tableDatas",classData);
        return ResultVOUtil.success(map);
    }

    /**
     *  通过课程ID,用户ID获取课程的分数
     * @return 该课程下的实验分数。
     */
    @GetMapping("/score/{courseId}")
    public ResultVO scoreList(@PathVariable(value = "courseId") Integer courseId,
                              @RequestParam(value = "userId") Integer userId){
        //获取学生信息
        UserDTO user = userService.selectOne(userId);
        //获取课程成绩信息
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
        HashMap<String,Object> map = new HashMap<>();
        map.put("experiments",experiments);
        map.put("score",score);
        map.put("headUrl",user.getHeadUrl());
        map.put("nickName",user.getNickname());
        return ResultVOUtil.success(map);
    }

    @GetMapping("/judge/{courseId}")
    public ResultVO<HashMap> judge(@PathVariable(value = "courseId", required = true) Integer courseId,
                                   @RequestParam(value = "experimentId", required = false) Integer experimentId,
                                   @RequestParam(value = "classId", required = false) Integer classId) {
        HashMap<String,Object> map=new HashMap<>();
        //根据课程id  获取实验列表
        List<ExperimentDTO> experimentDTOList= experimentService.list(courseId);
        if(experimentDTOList==null||experimentDTOList.isEmpty()){
            return ResultVOUtil.success(map);
        }
        //首次访问--获取实验列表
        if (experimentId==null){
            List<HashMap> experimentList=new ArrayList<>();
            for(ExperimentDTO experimentDTO:experimentDTOList){
                HashMap<String,Object> experimentMap=new HashMap<>();
                experimentMap.put("experimentId",experimentDTO.getExperimentId());
                experimentMap.put("experimentName",experimentDTO.getExperimentName());
                experimentList.add(experimentMap);
            }
            map.put("experimentList",experimentList);
            //获取实验的提交记录    缺省默认查找实验列表中的第一个实验
            if (!experimentList.isEmpty()){
                experimentId=(Integer) experimentList.get(0).get("experimentId");
            }
        }
        List<AchievementDTO> achievementDTOList = achievementService.getAchievementByCourseId(courseId);
        if (achievementDTOList==null||achievementDTOList.isEmpty()){
            return ResultVOUtil.success(map);
        }
        //班级列表
        Set<Integer> classIdSet=achievementDTOList.stream().map(achievement -> achievement.getClassId()).collect(Collectors.toSet());
        //获取班级列表
        List<HashMap> classList=new ArrayList<>();
        if(classId==null||classId==0){
            for(Integer clazzId:classIdSet){
                Class classByClazzId = classService.selectOne(clazzId);
                HashMap<String,Object> clazzMap=new HashMap<>();
                clazzMap.put("classId",classByClazzId.getClassId());
                clazzMap.put("className",classByClazzId.getClassName());
                classList.add(clazzMap);
            }
            //首次访问 --返回班级列表
            if(classId==null){
                map.put("classList",classList);
            }
        }else{
            Class classByClazzId = classService.selectOne(classId);
            HashMap<String,Object> clazzMap=new HashMap<>();
            clazzMap.put("classId",classByClazzId.getClassId());
            clazzMap.put("className",classByClazzId.getClassName());
            classList.add(clazzMap);
        }
        List<RecordDTO> recordDTOS  = recordService.getRecordListByExperimentId(experimentId);
        if (recordDTOS==null||recordDTOS.isEmpty()){
            return ResultVOUtil.success(map);
        }
        //获取学生列表
        List<JudgeVO> judgeVOList= recordDTOS.stream().map(recordDTO -> {
            JudgeVO judgeVO=new JudgeVO();
            UserDTO user = userService.selectOne(recordDTO.getUserId());
            judgeVO.setClassId(user.getClassId());
            judgeVO.setUserId(user.getUserId());
            judgeVO.setUserNumber(user.getUserNumber());
            judgeVO.setNickName(user.getNickname());
            judgeVO.setUserExperimentId(recordDTO.getUserExperimentId());
            judgeVO.setStatus(recordDTO.getStatus().intValue());
            return judgeVO;
        }).collect(Collectors.toList());

        //按班级分割
        List<HashMap> classData=new ArrayList<>(classList.size());
        for(HashMap clazz:classList) {
            HashMap<String,Object> clazzMap=new HashMap<>();
            Integer clazzId=(Integer) clazz.get("classId");
            String clazzName=(String) clazz.get("className");
            List<JudgeVO> judgeVO = judgeVOList.stream().filter(a->a.getClassId().equals(clazzId) )
                    .collect(Collectors.toList());
            clazzMap.put("classId",clazzId);
            clazzMap.put("className",clazzName);
            clazzMap.put("userList",judgeVO);
            classData.add(clazzMap);
        }
        map.put("tableDatas",classData);
        return ResultVOUtil.success(map);
    }

    @GetMapping("/judge/detail/{experimentId}")
    public ResultVO<RecordVO> detail(@PathVariable(value = "experimentId") Integer experimentId,
                                     @RequestParam(value = "userId") Integer userId) {
        RecordVO recordVO=new RecordVO();
        UserDTO user = userService.selectOne(userId);
        RecordDTO recordDTO = recordService.selectOne(experimentId, userId);
        if (recordDTO!=null){
            BeanUtils.copyProperties(recordDTO,recordVO);
        }
        recordVO.setUserName(user.getNickname());
        recordVO.setUserNumber(user.getUserNumber());
        recordVO.setHeadUrl(user.getHeadUrl());
        return ResultVOUtil.success(recordVO);
    }

    @PostMapping("/judge/save")
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
    @GetMapping("/judge/batch/{experimentId}")
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
            Class classByClazzId = classService.selectOne(clazzId);
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
            UserDTO user = userService.selectOne(record.getUserId());
            judgeVO.setClassId(user.getClassId());
            judgeVO.setUserId(user.getUserId());
            judgeVO.setUserNumber(user.getUserNumber());
            judgeVO.setNickName(user.getNickname());
            judgeVO.setStatus(record.getStatus().intValue());
            judgeVO.setUserExperimentId(record.getUserExperimentId());
            return judgeVO;
        }).collect(Collectors.toList());
        //按班级分割
        List<HashMap> classData=new ArrayList<>(classList.size());
        for(HashMap clazz:classList){
            //根据班级id筛选结果
            HashMap<String,Object> clazzMap=new HashMap<>();
            Integer clazzId=(Integer) clazz.get("classId");
            String clazzName=(String)clazz.get("className");
            List<JudgeVO> judgeVO = judgeVOList.stream().filter(a->a.getClassId().equals(clazzId))
                    .map(judgeVO1 -> {judgeVO1.setClassName(clazzName);return judgeVO1;})
                    .collect(Collectors.toList());
            clazzMap.put("classId",clazzId);
            clazzMap.put("className",clazzName);
            clazzMap.put("userList",judgeVO);
            classData.add(clazzMap);
        }
        map.put("tableDatas",classData);
        return ResultVOUtil.success(map);
    }


    @PostMapping("/judge/batch/save")
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
    public String export(HttpServletResponse response,@PathVariable(value = "courseId") Integer courseId) throws IOException {
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        CourseDTO course = courseService.selectOne(courseId);
        response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(course.getCourseName()+"成绩表.xls","UTF-8"));
        OutputStream outputStream = response.getOutputStream();
        try{
            List<List<String>> collect = achievementService.exportAchievement(course);
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
            Workbook sheet = poiUtil.createSheet(course.getCourseName()+"成绩表",colunNames,collect);
            sheet.write(outputStream);
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            outputStream.close();
        }


        System.out.println("success");
        return null;
    }
    @GetMapping("/judge/getOneComment")
    @ResponseBody
    public ResultVO getComment(@RequestParam Double score){
        return ResultVOUtil.success(redisService.getOneComment(score));
    }
}
