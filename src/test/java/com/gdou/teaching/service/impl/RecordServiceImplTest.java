package com.gdou.teaching.service.impl;

import com.gdou.teaching.dto.RecordDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

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
    @Transactional
    void save() {
        RecordDTO recordDTO = new RecordDTO();
        recordDTO.setUserId(3);
        recordDTO.setClassId(1);
        recordDTO.setHaveCheckAnswer(false);
        recordDTO.setExperimentId(1);
        recordDTO.setExperimentName("实验一 不认识树");
        recordDTO.setUserExperimentText("实验答案，这不是我的提交记录");
        RecordDTO save = recordService.save(recordDTO);
        Assert.notNull(save.getUserExperimentId(),"error ");
        log.info("RecordDTO with id :{}",save);
    }

    @Test
    void selectOne() {
        RecordDTO recordDTO = recordService.selectOne(1, 20);
        Assert.notNull(recordDTO,"error");
        log.info("RecordDTO :{}",recordDTO);
    }

    @Test
    void getRecordByUserIdAndCourseId() {
        List<RecordDTO> recordByUserIdAndCourseId = recordService.getRecordByUserIdAndCourseId(17,4);
        Assert.notNull(recordByUserIdAndCourseId,"error");
        log.info("RecordDTO :{}",recordByUserIdAndCourseId);
    }
    @Test
    @Transactional
    void judge(){
        RecordDTO recordDTO = new RecordDTO();
        recordDTO.setUserExperimentId(2);
        recordDTO.setExperimentAchievement(80d);
        recordDTO.setUserId(17);
        recordDTO.setHaveCheckAnswer(false);
        recordDTO.setExperimentId(2);
        recordService.judge(recordDTO);

    }

    @Test
    void batchJudge(){
        List<RecordDTO> recordDTOList=new ArrayList<>();
        RecordDTO recordDTO1 = recordService.selectOne(1, 17);
        recordDTO1.setExperimentAchievement(30d);
        RecordDTO recordDTO2 = recordService.selectOne(1, 20);
        recordDTO2.setExperimentAchievement(60d);
        recordDTOList.add(recordDTO1);
        recordDTOList.add(recordDTO2);

        recordService.batchJudge(recordDTOList);
    }

}