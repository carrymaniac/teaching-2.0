package com.gdou.teaching.controller.common;

import com.gdou.teaching.constant.CommonConstant;
import com.gdou.teaching.dataobject.Event;
import com.gdou.teaching.dto.ExperimentDTO;
import com.gdou.teaching.event.EventProducer;
import com.gdou.teaching.service.ExperimentService;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.controller.common
 * @ClassName: TestController
 * @Author: carrymaniac
 * @Description: 控制器
 * @Date: 2019/12/30 3:10 下午
 * @Version:
 */
@Controller
@RequestMapping("/test")
public class TestController {


    private final ExperimentService experimentService;
    private final EventProducer eventProducer;

    public TestController(ExperimentService experimentService, EventProducer eventProducer) {
        this.experimentService = experimentService;
        this.eventProducer = eventProducer;
    }

    @GetMapping("/ForExperimentList")
    @ResponseBody
    public ResultVO ForExperimentList(@RequestParam(value = "courseId")Integer courseId){
        List<ExperimentDTO> list = experimentService.list(courseId);
        return ResultVOUtil.success(list);
    }


    @GetMapping("/FireEvent")
    @ResponseBody
    public ResultVO FireEvent(){
        eventProducer.fireCourseUpdateEvent(71,1);
        eventProducer.fireCourseUpdateEvent(71,2);
        eventProducer.fireCourseUpdateEvent(71,3);
        eventProducer.fireCourseUpdateEvent(71,4);
        return ResultVOUtil.success();
    }
}

