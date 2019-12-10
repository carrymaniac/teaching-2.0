package com.gdou.teaching.service.impl;

import com.gdou.teaching.Enum.CourseStatusEnum;
import com.gdou.teaching.dto.CourseDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.service.impl
 * @ClassName: CourseServiceImplTest
 * @Author: carrymaniac
 * @Description:
 * @Date: 2019/12/9 3:22 下午
 * @Version:
 */
@Slf4j
@SpringBootTest
class CourseSewrviceImplTest {

    @Autowired
    CourseServiceImpl courseService;
    @Test
    void info() {
    }

    @Test
    void save() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setTeacherNickname("一学铭");
        courseDTO.setTeacherId(5);
        courseDTO.setCourseName("数据结构与算法");
        courseDTO.setCourseCredit(5.0);
        courseDTO.setCourseIntroduction("这课程🐂🍺疯了");
        courseDTO.setCourseCode("XSWL233");
        courseDTO.setCourseNumber(60);
        courseDTO.setCourseStatus(CourseStatusEnum.NORMAL.getCode().byteValue());
        courseService.save(courseDTO);
    }

    @Test
    void getCourseByUserId() {
        List<CourseDTO> courseByUserId = courseService.getCourseByUserId(5);
        Assert.isTrue(!courseByUserId.isEmpty(),"courseByUserId is null");
        log.info("courseByUserId is {}",courseByUserId);
    }

    @Test
    void detail() {
        CourseDTO detail = courseService.detail(1);
        Assert.notNull(detail,"detail is null");
        log.info("detail is {}",detail);
    }

    @Test
    void invalid() {
    }

    @Test
    void restore() {
    }

    @Test
    void list() {
    }
}