package com.gdou.teaching.service;

import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.mbg.model.User;

import java.util.List;
import java.util.Map;

/**
 * @author carrymaniac
 * @date Created in 19:23 2019-07-27
 * @description 用户类Serivce interface
 **/
public interface UserService {
    /**
     * 通过ID获取用户
     * @param id
     * @return
     */
    User getUserById(int id);

    /**
     * 注册
     * @param user
     * @return
     */
    Boolean register(User user);

    /**
     * 登陆
     * @param username
     * @param password
     * @return
     */
    User login(String username, String password);

    /**
     * 通过用户工号获取用户
     * @param userNumber
     * @return
     */
    User getUserByUserNumber(String userNumber);
    /**
     * 批量增加用户
     * @return
     */
    Boolean addUserByBatch(List<User> users);

    /**
     * 通过用户工号学号，批量删除用户
     * @param userNumbers
     * @return
     */
    Boolean deleteUserByBatch(List<String> userNumbers);

    /**
     * 通过用户ID查询用户详细信息
     * @param userId
     * @return
     */
    UserDTO getUserDetailByUserId(Integer userId);


    /**
     * 查询所有的教师列表
     */
    List<Map.Entry<Integer, String>> selectTeacherList();

//    List<UserDTO> getUsersByUserId(List<Integer> userIds);

    /**
     * 更新密码
     * @param userId
     * @param newPassword
     * @return
     */
    Boolean updatePassword(Integer userId,String oldPassword ,String newPassword);

}
