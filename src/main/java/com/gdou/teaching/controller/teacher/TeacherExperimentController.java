package com.gdou.teaching.controller.teacher;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.dto.CourseDTO;
import com.gdou.teaching.dto.ExperimentDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.form.ExperimentForm;
import com.gdou.teaching.service.CourseService;
import com.gdou.teaching.service.ExperimentService;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.CourseVO;
import com.gdou.teaching.vo.ExperimentVO;
import com.gdou.teaching.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
public class TeacherExperimentController {
    @Autowired
    private ExperimentService experimentService;
    @Autowired
    private CourseService courseService;

    /**
     * 实验列表
     * @param courseId
     * @return
     */
    @GetMapping(path = "/list/{courseId}")
    public ResultVO<CourseVO> list(@PathVariable(value = "courseId") Integer courseId) {
        CourseDTO courseDTO;
        List<ExperimentDTO> experimentDTOList;
        try {
            //查询课程基本信息
            courseDTO= courseService.info(courseId);
            //查询实验列表信息
            experimentDTOList=experimentService.list(courseId);
        } catch (TeachingException e) {
            log.error("[StudentCourseController]查询课程详情, 查询异常" + e.getMessage());
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMsg());
        }
        courseDTO.setCourseName(null);
        courseDTO.setCourseDetailId(null);
        courseDTO.setCourseStatus(null);
        courseDTO.setTeacherId(null);
        CourseVO courseVO = new CourseVO();
        BeanUtils.copyProperties(courseDTO, courseVO);

        List<ExperimentVO> ExperimentVOList = experimentDTOList.stream().map(experimentDTO -> {
            ExperimentVO experimentVO = new ExperimentVO();
            BeanUtils.copyProperties(experimentDTO, experimentVO);
            //设置不需要的字段为空
            experimentVO.setCourseId(null);
            experimentVO.setRecordStatus(null);
            experimentVO.setExperimentText(null);
            return experimentVO;
        }).collect(Collectors.toList());
        courseVO.setExperimentDTOList(ExperimentVOList);
        return ResultVOUtil.success(courseVO);
    }

    /**
     * 实验详情
     * @param experimentId
     * @return
     */
    @GetMapping("/detail/{experimentId}")
    public ResultVO<ExperimentDTO> detail(@PathVariable("experimentId") Integer experimentId) {
        ExperimentDTO experimentDTO=new ExperimentDTO();
        try {
            experimentDTO = experimentService.detail(experimentId);
            //将不需要的字段置空
            experimentDTO.setExperimentIntro(null);
        } catch (TeachingException e) {
            log.error("查询实验详情, 查询异常" + e.getMessage());
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMsg());
        }
        return ResultVOUtil.success(experimentDTO);
    }

    @GetMapping("/index")
    public ResultVO<ExperimentDTO> index(@RequestParam(value = "experimentId", required = false) Integer experimentId) {
        ExperimentDTO experimentDTO=null;
        //如果传入courseId不为空,为更新操作
        if (!StringUtils.isEmpty(experimentId)){
            try{
                experimentDTO = experimentService.detail(experimentId);
            }catch (TeachingException e) {
                log.error("查询实验详情,查询异常" + e.getMessage());
                return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMsg());
            }
        }
        return ResultVOUtil.success(experimentDTO);
    }

    @PostMapping("/save")
    public ResultVO save(@RequestBody @Valid ExperimentForm form,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("参数不正确：{}" + form);
            throw new TeachingException(ResultEnum.BAD_REQUEST.getCode(), ResultEnum.BAD_REQUEST.getMsg());
        }
        ExperimentDTO experimentDTO = new ExperimentDTO();
        BeanUtils.copyProperties(form, experimentDTO);
        try {
            experimentService.save(experimentDTO);
        } catch (TeachingException e) {
            log.error("保存实验,发生异常:{}", e);
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMsg());
        }
        return ResultVOUtil.success();
    }


    @GetMapping("/invalid/{experimentId}")
    public ResultVO invalid(@PathVariable("experimentId") Integer experimentId) {
        try {
            experimentService.invalid(experimentId);
        } catch (TeachingException e) {
            log.error("删除实验,发生异常");
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMsg());
        }
        return ResultVOUtil.success();
    }

    @GetMapping("/end/{experimentId}")
    public ResultVO end(@PathVariable("experimentId") Integer experimentId) {
        try {
            experimentService.end(experimentId);
        } catch (TeachingException e) {
            log.error("禁用实验,发生异常");
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMsg());
        }
        return ResultVOUtil.success();
    }

    @GetMapping("/lock/{experimentId}")
    public ResultVO lock(@PathVariable("experimentId") Integer experimentId) {
        try {
            experimentService.lock(experimentId);
        } catch (TeachingException e) {
            log.error("锁定实验,发生异常");
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMsg());
        }
        return ResultVOUtil.success();
    }

    @GetMapping("/unlock/{experimentId}")
    public ResultVO unlock(@PathVariable("experimentId") Integer experimentId) {
        try {
            experimentService.unlock(experimentId);
        } catch (TeachingException e) {
            log.error("解锁实验,发生异常");
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMsg());
        }
        return ResultVOUtil.success();
    }

}

