package com.gdou.teaching.service.impl;

import com.gdou.teaching.Enum.UserIdentEnum;
import com.gdou.teaching.Enum.UserStatusEnum;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.mbg.model.User;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.service.impl
 * @ClassName: UserServiceImplTest
 * @Author: carrymaniac
 * @Description:
 * @Date: 2019/12/9 10:52 上午
 * @Version:
 */
@SpringBootTest
@Slf4j
class UserServiceImplTest {

    @Autowired
    UserServiceImpl userService;
    @Test
    void getUserById() {
        User userById = userService.getUserById(17);
        Assert.notNull(userById,"error");
        log.info("user:{}",userById);
    }

    @Test
//    @Transactional
    void register() {
        User user = new User();
        user.setClassId(0);
        user.setNickname("涂涂超");
        user.setUserIdent(UserIdentEnum.TEACHER.getCode().byteValue());
        user.setUserNumber("8885867");
        user.setPassword("123456");
        Boolean register = userService.addUser(user);

    }

    @Test
    void login() {
        User login = userService.login("201611671301", "123456");
        log.info("登陆情况：{}",login);
    }

    @Test
    void getUserByUserNumber() {
        User userByUserNumber = userService.getUserByUserNumber("201611671309");
        Assert.notNull(userByUserNumber,"userNumberFor 201611671309");
        log.info("userNumberFor 201611671309 is : {}",userByUserNumber);
    }

    @Test
    void addUserByBatch() {
        User user = new User();
        user.setClassId(1);
        user.setNickname("小白");
        user.setUserIdent(UserIdentEnum.SUTUDENT.getCode().byteValue());
        user.setUserNumber("201611671301");
        user.setPassword("123456");
        user.setHeadUrl("http://images.nowcoder.com/head/325t.png");
        user.setSalt("81f59");
        user.setPassword("4a600be270e2b8e1321adc4cc99f125f");
        user.setUserStatus(UserStatusEnum.NORMAL.getCode().byteValue());
        try{
            userService.addUserByBatch(Arrays.asList(user));
        }catch (TeachingException e){
            log.info(e.getMessage());
        }
    }

    @Test
    void deleteUserByBatch() {
    }

    @Test
    void getUserDetailByUserId() {
    }

    @Test
    void selectTeacherList() {
    }

    @Test
    void updatePassword() {
    }

    @Test
    void selectTeacherListByPage(){
        PageInfo pageInfo = userService.selectTeacherListByPage(1, 10);
        log.info("pageInfo:{}",pageInfo);
    }
}