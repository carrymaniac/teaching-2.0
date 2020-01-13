package com.gdou.teaching.mbg.mapper;

import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.mbg.model.UserExample;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.mbg.mapper
 * @ClassName: UserMapperTest
 * @Author: carrymaniac
 * @Description:
 * @Date: 2019/12/2 4:40 下午
 * @Version:
 */
@SpringBootTest
@Slf4j
class UserMapperTest {
    @Autowired
    UserMapper userMapper;
    @Test
    void countByExample() {
    }

    @Test
    void deleteByExample() {
    }

    @Test
    void deleteByPrimaryKey() {
    }

    @Test
    void insert() {
        User user = new User();
        user.setUserNumber("201611671309");
        user.setUserIdent((byte) 0);
        user.setClassId(2);
        user.setHeadUrl("http://images.nowcoder.com/head/325t.png");
        user.setSalt("81f59");
        user.setPassword("4a600be270e2b8e1321adc4cc99f125f");
        user.setNickname("john wick");
        user.setUserStatus((byte) 0);
        userMapper.insert(user);
    }

    @Test
    void insertSelective() {

    }

    @Test
    void selectByExample() {

    }

    @Test
    void selectByPrimaryKey() {

    }

    @Test
    void updateByExampleSelective() {
    }
}