package com.gdou.teaching.mbg.mapper;

import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.mbg.model.UserExample;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        user.setUserIdent((byte) 0);
        user.setClassId(2);
        user.setHeadUrl("http://images.nowcoder.com/head/325t.png");
        user.setSalt("81f59");
        user.setPassword("4a600be270e2b8e1321adc4cc99f125f");
        user.setUserStatus((byte) 0);
        for(int i = 0;i<1024;i++){
            user.setUserNumber("20771024"+i);
            user.setNickname("测试学生用户"+i);
            userMapper.insert(user);
        }
    }

    @Test
    void insertSelective() {

    }

    @Test
    void selectByExample() {
        UserExample example = new UserExample();
        example.createCriteria().andClassIdEqualTo(1).andNicknameEqualTo("233");
        List<User> users = userMapper.selectByExample(example);
    }

    @Test
    void selectByPrimaryKey() {

    }

    @Test
    void updateByExampleSelective() {
    }
}