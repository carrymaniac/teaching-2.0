package com.gdou.teaching.service.impl;

import com.gdou.teaching.Enum.AnswerStatusEnum;
import com.gdou.teaching.dto.AnswerDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.service.impl
 * @ClassName: AnswerServiceImplTest
 * @Author: carrymaniac
 * @Description:
 * @Date: 2019/12/10 8:52 下午
 * @Version:
 */
@SpringBootTest
@Slf4j
class AnswerServiceImplTest {

    @Autowired
    AnswerServiceImpl answerService;
    @Test
    void detail() {
        AnswerDTO detail = answerService.detail(1);
        Assert.notNull(detail,"error");
        log.info("detail: {}",detail);
    }

    @Test
    @Transactional
    void save() {
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setExperimentAnswerContent("这是一个🌲的实验答案");
        answerDTO.setExperimentId(1);
        answerDTO.setExperimentAnswerStatus(AnswerStatusEnum.NORMAL.getCode().byteValue());
        answerService.save(answerDTO);
    }
}