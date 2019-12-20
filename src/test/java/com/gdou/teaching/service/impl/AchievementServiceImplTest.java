package com.gdou.teaching.service.impl;

import com.gdou.teaching.dto.AchievementDTO;
import com.gdou.teaching.mbg.mapper.AchievementMapper;
import com.gdou.teaching.mbg.model.Achievement;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.service.impl
 * @ClassName: AchievementServiceImplTest
 * @Author: carrymaniac
 * @Description:
 * @Date: 2019/12/9 8:17 下午
 * @Version:
 */
@SpringBootTest
@Slf4j
class AchievementServiceImplTest {

    @Autowired
    AchievementServiceImpl achievementService;
    @Test
    void addAchievementByClazzId() {

    }

    @Test
    @Transactional
    void addAchievementByStudentList() {
        boolean b = achievementService.addAchievementByStudentList(1, Arrays.asList(1, 3));
        Assert.isTrue(b,"error");
    }

    @Test
    void deleteAchievementByStudentList() {
    }

    @Test
    void getCourseNumberByUserId() {
        Integer courseNumberByUserId = achievementService.getCourseNumberByUserId(1);
        log.info("number:{}",courseNumberByUserId);
    }

    @Test
    void getAchievementByUserIdAndCourseId() {
        Achievement achievementByUserIdAndCourseId = achievementService.getAchievementByUserIdAndCourseId(1, 1);
        Assert.notNull(achievementByUserIdAndCourseId,"error!");
        log.info("Achievement:{}",achievementByUserIdAndCourseId);
    }

    @Test
    void getAchievementByCourseId() {
        List<AchievementDTO> achievementByCourseId = achievementService.getAchievementByCourseId(1);
        Assert.isTrue(!achievementByCourseId.isEmpty(),"error");
        log.info("AchievementList:{}",achievementByCourseId);
    }

    @Test
    void updateAchievement(){
        achievementService.updateAchievement(1,3);
    }
}