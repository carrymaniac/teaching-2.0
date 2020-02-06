package com.gdou.teaching.controller.teacher;

import com.gdou.teaching.Enum.FileCategoryEnum;
import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.dto.AnswerDTO;
import com.gdou.teaching.dto.CourseDTO;
import com.gdou.teaching.dto.ExperimentDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.form.*;
import com.gdou.teaching.service.AnswerService;
import com.gdou.teaching.service.CourseService;
import com.gdou.teaching.service.ExperimentService;
import com.gdou.teaching.service.FileService;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.CourseMainPageVO;
import com.gdou.teaching.vo.ExperimentVO;
import com.gdou.teaching.vo.ResultVO;
import com.gdou.teaching.web.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bo
 * @date Created in 18:08 2019/8/19
 * @description
 **/
@RestController
@RequestMapping("/teacher/experiment")
@Slf4j
@Auth
public class TeacherExperimentController {
    @Autowired
    private ExperimentService experimentService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private FileService fileService;
    /**
     * 实验列表
     * @param courseId
     * @return
     */
    @GetMapping(path = "/list/{courseId}")
    public ResultVO<CourseMainPageVO> list(@PathVariable(value = "courseId") Integer courseId) {
        //查询课程基本信息
        CourseDTO  courseDTO= courseService.selectOne(courseId);
        CourseMainPageVO courseMainPageVO = new CourseMainPageVO();
        BeanUtils.copyProperties(courseDTO,courseMainPageVO);
        // 查询实验列表信息
        List<ExperimentDTO> experimentDTOList=experimentService.list(courseId);
        if(experimentDTOList==null){
            return ResultVOUtil.success(courseMainPageVO);
        }
        List<ExperimentVO> ExperimentVOList = experimentDTOList.stream().map(experimentDTO -> {
            ExperimentVO experimentVO = new ExperimentVO();
            BeanUtils.copyProperties(experimentDTO, experimentVO);
            //设置不需要的字段为空
            experimentVO.setCourseId(null);
            experimentVO.setRecordStatus(null);
            experimentVO.setExperimentText(null);
            return experimentVO;
        }).collect(Collectors.toList());
        courseMainPageVO.setExperimentDTOList(ExperimentVOList);
        return ResultVOUtil.success(courseMainPageVO);
    }

    /**
     * 实验详情
     * @param experimentId
     * @return
     */
    @GetMapping("/detail/{experimentId}")
    public ResultVO<ExperimentDTO> detail(@PathVariable("experimentId") Integer experimentId) {
        ExperimentDTO experimentDTO=new ExperimentDTO();
        experimentDTO = experimentService.detail(experimentId);
        if(experimentDTO.getExperimentAnswerId()!=null){
            AnswerDTO answerDTO = answerService.selectOne(experimentDTO.getExperimentAnswerId());
            experimentDTO.setExperimentAnswerFile(answerDTO.getExperimentAnswerFileList()==null?new ArrayList<>():answerDTO.getExperimentAnswerFileList());
            experimentDTO.setExperimentAnswerContent(answerDTO.getExperimentAnswerContent());
        }else{
            experimentDTO.setExperimentAnswerFile(new ArrayList<>());
        }
        //将不需要的字段置空
        experimentDTO.setExperimentIntro(null);
        return ResultVOUtil.success(experimentDTO);
    }

    @GetMapping("/index")
    public ResultVO<ExperimentDTO> index(@RequestParam(value = "experimentId", required = false) Integer experimentId) {
        ExperimentDTO experimentDTO=null;
        //如果传入courseId不为空,为更新操作
        if (!StringUtils.isEmpty(experimentId)){
            experimentDTO = experimentService.detail(experimentId);
        }
        return ResultVOUtil.success(experimentDTO);
    }

    @PostMapping("/add")
    public ResultVO add(@RequestBody @Valid ExperimentForm form,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("参数不正确：{}" + form);
            throw new TeachingException(ResultEnum.BAD_REQUEST.getCode(), ResultEnum.BAD_REQUEST.getMsg());
        }
        ExperimentDTO experimentDTO = new ExperimentDTO();
        BeanUtils.copyProperties(form, experimentDTO);
        experimentService.add(experimentDTO);
        return ResultVOUtil.success();
    }
    @PostMapping("/updateExperimentInfo")
    public ResultVO updateExperimentInfo(@RequestBody @Valid ExperimentInfoUpdateForm form,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("参数不正确：{}" + form);
            throw new TeachingException(ResultEnum.BAD_REQUEST.getCode(), ResultEnum.BAD_REQUEST.getMsg());
        }
        ExperimentDTO experimentDTO = new ExperimentDTO();
        BeanUtils.copyProperties(form, experimentDTO);
        experimentService.updateExperimentInfo(experimentDTO);
        return ResultVOUtil.success();
    }

    @PostMapping("/updateExperimentDetail")
    public ResultVO updateExperimentDetail(@RequestBody @Valid ExperimentDetailUpdateForm form,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("参数不正确：{}" + form);
            throw new TeachingException(ResultEnum.BAD_REQUEST.getCode(), ResultEnum.BAD_REQUEST.getMsg());
        }
        ExperimentDTO experimentDTO = new ExperimentDTO();
        BeanUtils.copyProperties(form, experimentDTO);
        experimentService.updateExperimentDetail(experimentDTO);
        return ResultVOUtil.success();
    }

    @PostMapping("/updateExperimentFile")
    public ResultVO updateExperimentFile(@RequestBody @Valid ExperimentFileUpdateForm form,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("参数不正确：{}" + form);
            throw new TeachingException(ResultEnum.BAD_REQUEST.getCode(), ResultEnum.BAD_REQUEST.getMsg());
        }
        //先删除后新增 todo 等待前端协调
        fileService.deleteFiles(FileCategoryEnum.EXPERIMENT_FILE.getCode(),form.getExperimentId());
        fileService.saveFile(FileCategoryEnum.EXPERIMENT_FILE.getCode(),form.getExperimentId(),form.getExperimentDetailFile());
        return ResultVOUtil.success();
    }
    @PostMapping("/updateExperimentAnswer")
    public ResultVO updateExperimentAnswer(@RequestBody @Valid ExperimentAnswerUpdateForm form,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("参数不正确：{}" + form);
            throw new TeachingException(ResultEnum.BAD_REQUEST.getCode(), ResultEnum.BAD_REQUEST.getMsg());
        }
        ExperimentDTO experimentDTO = new ExperimentDTO();
        BeanUtils.copyProperties(form, experimentDTO);
        answerService.updateExperimentAnswer(experimentDTO);
        //更新答案阈值
        experimentService.updateExperimentInfo(experimentDTO);
        return ResultVOUtil.success();
    }
    @PostMapping("/updateExperimentAnswerFile")
    public ResultVO updateExperimentAnswerFile(@RequestBody @Valid ExperimentAnswerFileUpdateForm form,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("参数不正确：{}" + form);
            throw new TeachingException(ResultEnum.BAD_REQUEST.getCode(), ResultEnum.BAD_REQUEST.getMsg());
        }
        //先删除后增加 todo 等待前端协调
        fileService.deleteFiles(FileCategoryEnum.EXPERIMENT_ANSWER_FILE.getCode(),form.getExperimentAnswerId());
        fileService.saveFile(FileCategoryEnum.EXPERIMENT_ANSWER_FILE.getCode(),form.getExperimentAnswerId(),form.getExperimentAnswerFile());
        return ResultVOUtil.success();
    }



    @GetMapping("/invalid/{experimentId}")
    public ResultVO invalid(@PathVariable("experimentId") Integer experimentId) {
        experimentService.invalid(experimentId);
        return ResultVOUtil.success();
    }

    @GetMapping("/end/{experimentId}")
    public ResultVO end(@PathVariable("experimentId") Integer experimentId) {
        experimentService.end(experimentId);
        return ResultVOUtil.success();
    }

    @GetMapping("/lock/{experimentId}")
    public ResultVO lock(@PathVariable("experimentId") Integer experimentId) {
        experimentService.lock(experimentId);
        return ResultVOUtil.success();
    }

    @GetMapping("/unlock/{experimentId}")
    public ResultVO unlock(@PathVariable("experimentId") Integer experimentId) {
        experimentService.unlock(experimentId);
        return ResultVOUtil.success();
    }

}

