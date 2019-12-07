package com.gdou.teaching.dao;

import com.gdou.teaching.mbg.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.dao
 * @ClassName: UserDaoTest
 * @Author: carrymaniac
 * @Description:
 * @Date: 2019/12/3 3:58 下午
 * @Version:
 */
@Slf4j
@SpringBootTest
class UserDaoTest {
    @Autowired
    UserDao userDao;

    @Test
    void insertList() {
    }

    @Test
    void selectByUserNumberLimitOne() {
        User user = userDao.selectByUserNumberLimitOne("201611671309");
        log.info("user is {}",user);
    }
}