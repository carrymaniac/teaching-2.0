package com.gdou.teaching.service;

import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.mbg.model.User;
import com.github.pagehelper.PageInfo;
import org.springframework.data.relational.core.sql.In;

import java.util.List;
import java.util.Map;

/**
 * @author carrymaniac
 * @date Created in 19:23 2019-07-27
 * @description 用户类Serivce interface
 **/
public interface UserService {
    /**
     * 注册
     * @param user
     * @return
     */
    Boolean addUser(User user);

    /**
     * 登陆
     * @param username
     * @param password
     * @return
     */
    User login(String username, String password);

    /**
     * 通过ID获取用户
     * @param id
     * @return
     */
    User getUserById(int id);

    /**
     * 通过用户工号获取用户
     * @param userNumber
     * @return
     */
    User getUserByUserNumber(String userNumber);

    /**
     * 通过用户ID查询用户详细信息
     * @param userId
     * @return
     */
    UserDTO getUserDetailByUserId(Integer userId);

    /**
     * 通过ID列表批量获取用户信息
     * @param userIds
     * @return
     */
    List<UserDTO> getUsersByUserId(List<Integer> userIds);

    /**
     * 批量增加用户
     * @param users
     * @return
     */
    Boolean addUserByBatch(List<User> users);

    /**
     * 批量增加用户信息
     * @param userIdList
     * @param college
     * @param series
     * @param major
     * @return
     */
    Boolean addUserInfoByBatch(List<Integer> userIdList,String college,String series,String major);

    /**
     * 通过用户工号学号，批量注销用户
     * @param userId
     * @return
     */
    Boolean deleteUserByBatch(List<Integer> userId);

    /**
     * 根据班级Id查询学生列表, 若classId为0即全查
     * @param classId
     * @return
     */
    List<UserDTO> getStudentListByClassId(Integer classId);

    /**
     * 根据班级Id查询学生列表, 若classId为0即全查,采用分页返回
     * @param classId
     * @param page
     * @param size
     * @return
     */
    PageInfo getStudentListByClassIdAndKeywordInPage(Integer classId,Integer page,Integer size,String keyword);

    /**
     * 查询所有的教师列表
     * @return
     */
    List<User> selectTeacherList();

    /**
     * 通过分页查询所有的教师列表
     * @param page
     * @param size
     * @return
     */
    PageInfo selectTeacherListByPage(Integer page, Integer size);


    /**
     * 更新密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否更新成功
     */
    Boolean updatePassword(Integer userId,String oldPassword ,String newPassword);

    /**
     * 重设密码
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return
     */
    Boolean resetPassword(Integer userId,String newPassword);


}
