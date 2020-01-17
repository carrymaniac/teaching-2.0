package com.gdou.teaching.controller.student;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.constant.RedisConstant;
import com.gdou.teaching.dataobject.HostHolder;
import com.gdou.teaching.dto.RecordDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.form.RecordForm;
import com.gdou.teaching.mbg.model.ExperimentMaster;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.service.ExperimentService;
import com.gdou.teaching.service.RecordService;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author bo
 * @date Created in 21:18 2019/9/12
 * @description
 **/
@RestController
@RequestMapping("/record")
@Slf4j
public class StudentRecordController {
    @Autowired
    RecordService recordService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ExperimentService experimentService;
    /**
     * 第一次提交完之后需要更新目前已提交的数字
     * @param form
     * @param bindingResult
     * @return
     */
    @PostMapping("/save")
    public ResultVO save(@RequestBody @Valid RecordForm form,
                         BindingResult bindingResult) {
        User user = hostHolder.getUser();
        if (bindingResult.hasErrors()) {
            log.error("参数不正确：{}" + form);
            throw new TeachingException(ResultEnum.PARAM_ERROR);
        }
        RecordDTO recordDTO = new RecordDTO();
        BeanUtils.copyProperties(form, recordDTO);
        recordDTO.setUserId(user.getUserId());
        recordDTO.setClassId(user.getClassId());
        if(form.getUserExperimentId()==null){
            //第一次提交，查询是否查看过答案
            String key = String.format(RedisConstant.BIZ_CHECK_ANSWER, form.getExperimentId());
            Boolean checkAnswer = redisTemplate.opsForSet().isMember(key, user.getUserId().toString());
            recordDTO.setHaveCheckAnswer(checkAnswer);
        }
        try {
            recordService.save(recordDTO);
            //UserExperimentId为空,为新增提交记录.
            if(form.getUserExperimentId()==null){
                //更新提交人数
                ExperimentMaster experimentMaster = experimentService.updateCommitNumber(form.getExperimentId());
                //判断解锁阈值, 提交人数/总人数>解锁阈值,则自动解锁下一个实验.
                if (experimentMaster.getExperimentCommitNum()*1.0/experimentMaster.getExperimentParticipationNum()>=experimentMaster.getValve()){
                    experimentService.autoUnLock(form.getExperimentId());
                }
            }
        } catch (TeachingException e) {
            log.error("保存记录,发生异常:{}", e);
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(),e.getMessage());
        }
        return ResultVOUtil.success();
    }
}
