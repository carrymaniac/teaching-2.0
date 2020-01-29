package com.gdou.teaching.controller.student;


import com.gdou.teaching.Enum.*;
import com.gdou.teaching.dataobject.*;
import com.gdou.teaching.dto.CourseDTO;
import com.gdou.teaching.dto.ExperimentDTO;
import com.gdou.teaching.dto.FileDTO;
import com.gdou.teaching.dto.RecordDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.mbg.model.Achievement;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.service.*;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.CourseMainPageVO;
import com.gdou.teaching.vo.CourseVO;
import com.gdou.teaching.vo.ExperimentVO;
import com.gdou.teaching.vo.ResultVO;
import com.gdou.teaching.web.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author bo
 * @date Created in 22:39 2019/8/14
 * @description
 **/
@RestController
@RequestMapping("/student/course")
@Slf4j
@Auth(user=UserIdentEnum.SUTUDENT)
public class StudentCourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private ExperimentService experimentService;
    @Autowired
    private AchievementService achievementService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private RecordService recordService;
    @Autowired
    private FileService fileService;


    /**
     * 课程主页
     * @param courseId 课程ID
     * 通过课程ID查询下属的实验列表，然后通过UserId和ExperimentID查询用户的完成情况。返回给前端
     * @return
     */
    @GetMapping(path = "/info/{courseId}")
     public ResultVO<CourseVO> info(@PathVariable(value = "courseId") Integer courseId) {
        if (courseId == null) {
            return ResultVOUtil.fail(ResultEnum.PARAM_NULL);
        }
        User user = hostHolder.getUser();
        try {
            //查询课程基本信息
            CourseDTO courseDTO = courseService.info(courseId);
            CourseMainPageVO courseMainPageVO = new CourseMainPageVO();
            BeanUtils.copyProperties(courseDTO, courseMainPageVO);
            //查询实验列表信息
            List<ExperimentDTO> experimentDTOList = experimentService.list(courseId);
            if(experimentDTOList!=null&&!experimentDTOList.isEmpty()){
                //结合用户提交记录，将DTO转为VO
                List<ExperimentVO> ExperimentVOList = experimentDTOList.stream().map(experimentDTO -> {
                    ExperimentVO experimentVO = new ExperimentVO();
                    BeanUtils.copyProperties(experimentDTO, experimentVO);
                    //设置不需要的字段为空
                    experimentVO.setCourseId(null);
                    //设置状态
                    if(ExperimentStatusEnum.LOCK.getCode().byteValue()==experimentDTO.getExperimentStatus()){
                        experimentVO.setRecordStatus(RecordStatusEnum.LOCK.getCode());
                    }else{
                        RecordDTO recordDTO = recordService.selectOne(experimentDTO.getExperimentId(), user.getUserId());
                        if(recordDTO!=null){
                            experimentVO.setRecordStatus(recordDTO.getStatus().intValue());
                        }else{
                            experimentVO.setRecordStatus(RecordStatusEnum.NOT_FINISH.getCode());
                        }
                    }
                    return experimentVO;
                }).collect(Collectors.toList());
                courseMainPageVO.setExperimentDTOList(ExperimentVOList);
            }
            return ResultVOUtil.success(courseMainPageVO);
        } catch (TeachingException e) {
            log.info("[StudentCourseController]查询课程主页, 查询异常:{}", e.getMessage());
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR);
        }
    }

    /**
     * 查询课程详情信息
     * @param courseId
     * @return
     */
    @GetMapping("/detail/{courseId}")
    public ResultVO detail(@PathVariable(value = "courseId") Integer courseId) {
        CourseDTO detail = courseService.detail(courseId);
        if(detail==null){
            log.info("[StudentCourseController]查询课程详情信息, 查询异常，courseId={}",courseId);
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR);
        }
        return ResultVOUtil.success(detail);
    }

    /**
     * 不分页版本-获取课程列表
     * 通过用户ID获取课程列表，并将其分割为结束和未结束两部分
     * @return
     */
    @GetMapping("/listForNoPage")
    public ResultVO listForNoPage(){
        User user = hostHolder.getUser();
        //通过ID获取到用户课程数据
        List<CourseDTO> list = courseService.listCourseForStudent(user.getUserId());
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
     *  通过课程ID获取该课程的分数
     * @return 该课程下的实验分数。
     */
    @GetMapping("/score/{courseId}")
    public ResultVO scoreList(@PathVariable(value = "courseId") Integer courseId){
        User user = hostHolder.getUser();
        Achievement achievement = achievementService.getAchievementByUserIdAndCourseId(user.getUserId(), courseId);
        Double score = achievement.getCourseAchievement();
        //通过课程ID和用户ID获取提交记录列表
        List<RecordDTO> recordByUserIdAndCourseId = recordService.getRecordByUserIdAndCourseId(user.getUserId(), courseId);
        if(recordByUserIdAndCourseId!=null){
            List<HashMap<String, String>> experiments = recordByUserIdAndCourseId.stream().map(r -> {
                HashMap<String, String> ex = new HashMap<>(3);
                ex.put("experimentName",r.getExperimentName());
                ex.put("experimentId", r.getExperimentId().toString());
                ex.put("experimentAchievement", r.getExperimentAchievement().toString());
                return ex;
            }).collect(Collectors.toList());
            //提取所需要的字段（实验名-实验ID-实验得分）返回
            //获取课程成绩信息
            HashMap<String,Object> map = new HashMap<>();
            map.put("experiments",experiments);
            map.put("score",score);
            return ResultVOUtil.success(map);
        }else {
            return ResultVOUtil.success();
        }
    }

    @GetMapping("/resource/{courseId}")
    public ResultVO resource(@PathVariable(value = "courseId") Integer courseId, @RequestParam(required = false)String keyword){
        if(StringUtils.isEmpty(keyword)){
            //通过课程ID获取课程关连的文件
                return ResultVOUtil.success(fileService.selectFileByCategoryAndFileCategoryId(FileCategoryEnum.COURSE_FILE.getCode(), courseId));
        }else {
            //通过关键字和课程ID获取关联的文件
                return ResultVOUtil.success(fileService.selectFileByCategoryAndFileCategoryIdAndKeyword(FileCategoryEnum.COURSE_FILE.getCode(),courseId,keyword));
        }
    }
}
