package com.gdou.teaching.controller.student;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.Enum.UserIdentEnum;
import com.gdou.teaching.constant.RedisConstant;
import com.gdou.teaching.dataobject.HostHolder;
import com.gdou.teaching.dto.AnswerDTO;
import com.gdou.teaching.dto.ExperimentDTO;
import com.gdou.teaching.dto.RecordDTO;
import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.service.AnswerService;
import com.gdou.teaching.service.ExperimentService;
import com.gdou.teaching.service.RecordService;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ResultVO;
import com.gdou.teaching.web.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.controller
 * @ClassName: StudentExperimentController
 * @Author: carrymaniac
 * @Description: 学生实验控制器
 * @Date: 2019/9/23 1:32 下午
 * @Version:
 */
@Controller
@Slf4j
@RequestMapping("/student/experiment/")
@Auth(user= UserIdentEnum.SUTUDENT)
public class StudentExperimentController {
    @Autowired
    ExperimentService experimentService;
    @Autowired
    RecordService recordService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    AnswerService answerService;
    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 获取实验信息
     * @param experimentId
     * @return
     */
    @GetMapping("/info/{experimentId}")
    @ResponseBody
    public ResultVO info(@PathVariable("experimentId") Integer experimentId) {
        UserDTO user = hostHolder.getUser();
        if (user == null) {
            return ResultVOUtil.fail(ResultEnum.USER_NO_LOGIN);
        }
        //实验的具体要求内容和实验文件
        ExperimentDTO detail = experimentService.detail(experimentId);
        if (detail == null) {
            return ResultVOUtil.fail(ResultEnum.EXPERIMENT_NOT_EXIST);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("experiment", detail);

        RecordDTO recordDTO = recordService.selectOne(experimentId, user.getUserId());
        //若用户为提交过没有记录，则不放提交记录在其中。
        if (recordDTO != null) {
            map.put("record", recordDTO);
        }
        return ResultVOUtil.success(map);
    }

    /**
     * 通过实验ID获取实验的标准答案
     * @param experimentAnswerId
     * 如果查询不为空，在Redis中将对应的查看答案标记将用户设置上去
     * 如果查询结果为空的话返回为空
     * @return
     */
    @GetMapping("/answer/{experimentAnswerId}")
    @ResponseBody
    public ResultVO answer(@PathVariable("experimentAnswerId")Integer experimentAnswerId){
        UserDTO user = hostHolder.getUser();
        AnswerDTO detail = answerService.selectOne(experimentAnswerId);
        if(detail!=null){
            //将用户ID和实验ID在Redis中进行一次标记，之后在Record的时候进行查看答案标记
            String key = String.format(RedisConstant.BIZ_CHECK_ANSWER, experimentAnswerId);
            redisTemplate.opsForSet().add(key,user.getUserId().toString());
            return ResultVOUtil.success(detail);
        }else {
            return ResultVOUtil.success(new AnswerDTO());
        }

    }
}
