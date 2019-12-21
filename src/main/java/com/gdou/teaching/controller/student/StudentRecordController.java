package com.gdou.teaching.controller.student;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.constant.RedisConstant;
import com.gdou.teaching.dataobject.HostHolder;
import com.gdou.teaching.dto.RecordDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.form.RecordForm;
import com.gdou.teaching.mbg.model.User;
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
        if(form.getUserExperimentId()==null){
            //第一次提交，查询是否查看过答案
            String key = String.format(RedisConstant.BIZ_CHECK_ANSWER, form.getExperimentId());
            Boolean checkAnswer = redisTemplate.opsForSet().isMember(key, user.getUserId().toString());
            recordDTO.setHaveCheckAnswer(checkAnswer);
        }
        try {
            recordService.save(recordDTO);
        } catch (TeachingException e) {
            log.error("保存记录,发生异常:{}", e);
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR.getCode(),e.getMessage());
        }
        return ResultVOUtil.success();
    }
}
