package com.gdou.teaching.service.impl;

import com.gdou.teaching.dto.ExperimentDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.service.impl
 * @ClassName: ExperimentServiceImplTest
 * @Author: carrymaniac
 * @Description:
 * @Date: 2019/12/9 8:27 下午
 * @Version:
 */
@SpringBootTest
@Slf4j
class ExperimentServiceImplTest {

    @Autowired
    ExperimentServiceImpl experimentService;
    @Test
    void detail() {
        ExperimentDTO detail = experimentService.detail(1);
        Assert.notNull(detail,"error for detail");
        log.info("detail:{}",detail);
    }

    @Test
    @Transactional
    void save() {
        ExperimentDTO experimentDTO = new ExperimentDTO();
        experimentDTO.setCourseId(1);
        experimentDTO.setExperimentIntro("关于数组和链表的实验");
        experimentDTO.setExperimentName("实验二 反转数组");
        experimentDTO.setExperimentText("如何反转一个数组");
        //experimentDTO.setExperimentId(8);
        //experimentDTO.setExperimentDetailId(13);
        ExperimentDTO save = experimentService.save(experimentDTO);
        log.info("save:{}",save);
    }

    @Test
    void list() {
        List<ExperimentDTO> list = experimentService.list(1);
        Assert.isTrue(!list.isEmpty(),"error ");
        log.info("list: {}",list);
    }

    @Test
    void invalid() {
    }

    @Test
    void ban() {
    }

    @Test
    void restore() {
    }

    @Test
    void lock() {
    }

    @Test
    void unlock() {
    }
}