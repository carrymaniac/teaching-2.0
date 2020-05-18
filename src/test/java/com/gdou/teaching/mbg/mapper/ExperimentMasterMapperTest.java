package com.gdou.teaching.mbg.mapper;

import com.gdou.teaching.Enum.ExperimentStatusEnum;
import com.gdou.teaching.mbg.model.ExperimentMaster;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.mbg.mapper
 * @ClassName: ExperimentMasterMapperTest
 * @Author: carrymaniac
 * @Description:
 * @Date: 2020/1/13 9:17 下午
 * @Version:
 */
@SpringBootTest
class ExperimentMasterMapperTest {
    @Autowired
    ExperimentMasterMapper experimentMasterMapper;
    @Test
    void insert() {
        ExperimentMaster experimentMaster = new ExperimentMaster();
        experimentMaster.setExperimentCommitNum(0);
        experimentMaster.setExperimentParticipationNum(60);
        experimentMaster.setCourseId(9);
        experimentMaster.setExperimentAnswerId(10);
        experimentMaster.setExperimentStatus(ExperimentStatusEnum.NORMAL.getCode().byteValue());
        Random random = new Random();
        experimentMaster.setExperimentName("hahahha");
        experimentMaster.setExperimentDetailId(10);
        experimentMaster.setValve((float) 0.6);
        experimentMaster.setPunishment((float) 0.6);
        for(int i = 0;i<300000;i++){
            experimentMaster.setExperimentIntro(""+random.nextInt(10000));
            experimentMasterMapper.insert(experimentMaster);
        }
    }
}