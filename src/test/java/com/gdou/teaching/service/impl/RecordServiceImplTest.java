package com.gdou.teaching.service.impl;

import com.gdou.teaching.dto.RecordDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.service.impl
 * @ClassName: RecordServiceImplTest
 * @Author: carrymaniac
 * @Description:
 * @Date: 2019/12/10 10:55 上午
 * @Version:
 */
@SpringBootTest
@Slf4j
class RecordServiceImplTest {

    @Autowired
    RecordServiceImpl recordService;
    @Test
    void save() {
        RecordDTO recordDTO = new RecordDTO();
        recordDTO.setUserId(3);
        recordDTO.setClassId(1);
        recordDTO.setHaveCheckAnswer(false);
        recordDTO.setExperimentId(1);
        recordDTO.setExperimentName("实验一 认识树");
        recordDTO.setUserExperimentText("实验答案，这是我的提交记录");
        RecordDTO save = recordService.save(recordDTO);
        Assert.notNull(save.getUserExperimentId(),"error ");
        log.info("RecordDTO with id :{}",save);
    }

    @Test
    void selectOne() {
    }

    @Test
    void updateExperimentCommitNumber() {
    }

    @Test
    void getRecordByUserIdAndCourseId() {
    }
}