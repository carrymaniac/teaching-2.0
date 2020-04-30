package com.gdou.teaching.service.impl;

import cn.hutool.json.JSONUtil;
import com.gdou.teaching.Enum.CourseStatusEnum;
import com.gdou.teaching.dto.CourseDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

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
class  CourseServiceImplTest {

    @Autowired
    CourseServiceImpl courseService;
    @Test
    void info() {
        CourseDTO info = courseService.selectOne(1);
        log.info("info is {}",info);
        Assert.notNull(info,"error");
    }

    @Test
//    @Transactional
    void save() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setTeacherNickname("涂涂超");
        courseDTO.setTeacherId(15);
        courseDTO.setCourseName("安卓与大数据2020");
        courseDTO.setCourseCredit(5.0);
        courseDTO.setCourseIntroduction("这课程牛逼疯了2020");
        courseDTO.setCourseCode("XSWL66662020");
        courseDTO.setCourseNumber(60);
        courseDTO.setCourseStatus(CourseStatusEnum.NORMAL.getCode().byteValue());
        courseDTO.setCourseCover("");
        CourseDTO save = courseService.save(courseDTO);
        Assert.notNull(save.getCourseId(),"error");
    }

    @Test
    void detail() {
        CourseDTO detail = courseService.detail(1);
        Assert.notNull(detail,"detail is null");
        log.info("detail is {}",detail);
    }

    @Test
    @Transactional
    void invalid() {
        boolean invalid = courseService.invalid(4);
        Assert.isTrue(invalid,"invalid is error");
    }



    @Test
    void list() {
        List<CourseDTO> list = courseService.listCourseByUserIdAndKeywordForStudent(3,"数据");
        log.info("list is {}", JSONUtil.toJsonPrettyStr(list));
//        Assert.notNull(list,"list is null");
    }

    @Test
    @Transactional
    void updateNumber(){
        courseService.updateNumber(100);
    }
}