package com.gdou.teaching.dao;

import com.gdou.teaching.mbg.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.dao
 * @ClassName: UserDao
 * @Author: carrymaniac
 * @Description: 自定义的用户类Dao
 * @Date: 2019/12/2 5:41 下午
 * @Version:
 */
@Repository
public interface UserDao {
    /**
     * 批量插入User用户，用于注册
     * @param users
     * @return
     */
    int insertList (List<User> users);

    /**
     * 通过学号查询用户且Limit 1
     * @param UserNumber
     * @return
     */
    User selectByUserNumberLimitOne(@Param("userNumber") String UserNumber);

    /**
     * 通过班级Id、身份、关键词模糊查询用户
     * @param classId 班级ID
     * @param keyWord 关键词
     * @param ident 身份
     * @return
     */
    List<User> selectByClassIdAndKeyword(@Param("classId")Integer classId, @Param("keyWord")String keyWord, @Param("ident")Integer ident);
}
