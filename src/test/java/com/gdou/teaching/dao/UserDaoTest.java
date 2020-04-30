package com.gdou.teaching.dao;

import com.gdou.teaching.Enum.UserIdentEnum;
import com.gdou.teaching.Enum.UserStatusEnum;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.mbg.model.UserExample;
import com.gdou.teaching.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
    @Autowired
    UserService userService;

    @Test
    void register(){
        User user=new User();
        user.setClassId(0);
        user.setUserIdent(UserIdentEnum.ADMIN.getCode().byteValue());
        user.setNickname("管理员03");
        user.setPassword("123456");
        user.setUserNumber("admin4096");
        user.setUserStatus(UserStatusEnum.NORMAL.getCode().byteValue());
        userService.addUser(user);
    }

    @Test
    void insertList() {
    }

    @Test
    void selectByUserNumberLimitOne() {
        User user = userDao.selectByUserNumberLimitOne("201611671309");
        log.info("user is {}",user);
    }

    @Test
    void selectByClassIdAndKeyWord(){
        List<User> userList = userDao.selectByClassIdAndKeyword(null, null, UserIdentEnum.TEACHER.getCode());
        System.out.println(userList.size());
    }
}