package com.gdou.teaching.service.impl;

import com.gdou.teaching.mbg.model.Class;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.service.impl.ClassServiceImpl;
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
 * @Package: com.gdou.teaching.service
 * @ClassName: ClazzServiceTest
 * @Author: carrymaniac
 * @Description:
 * @Date: 2019/12/9 2:15 下午
 * @Version:
 */
@SpringBootTest
@Slf4j
class ClassServiceImplTest {

    @Autowired
    ClassServiceImpl classService;

    @Test
    @Transactional
    void registerClass() {
        Class clazz = classService.registerClass("信管1164");
    }

    @Test
    void getClassByClazzId() {
        Class classByClazzId = classService.getClassByClazzId(1);
        Assert.notNull(classByClazzId,"error");
    }

    @Test
    void getClassesByPage() {

    }


    @Test
    @Transactional
    void updateClazz() {
        Class clazz = new Class();
        clazz.setClassName("信管1164");
        clazz.setClassId(1);
        Boolean aBoolean = classService.updateClazz(clazz);
        Assert.isTrue(aBoolean,"update class is false");
    }


    @Test
    void getStudentCountByClazzId() {
        Integer studentCountByClazzId = classService.getStudentCountByClazzId(1);
        Assert.notNull(studentCountByClazzId,"error");
    }
    @Test
    void getStudentByClazzId(){
        List<User> studentByClazzId = classService.getStudentByClazzId(1);
        Assert.notNull(studentByClazzId,"error");
    }

    @Test
    void getAllClazzList() {
        List<Class> allClazzList = classService.getAllClazzList();
        Assert.notNull(allClazzList,"getAllClazzList is null");
        log.info("getAllClazzList result: {}",allClazzList);
    }
}