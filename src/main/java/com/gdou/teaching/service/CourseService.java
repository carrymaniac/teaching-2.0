package com.gdou.teaching.service;


import com.gdou.teaching.dto.CourseDTO;

import java.util.List;

/**
 * @author carrymaniac
 * @date Created in 14:24 2019-08-14
 * @description
 **/
public interface CourseService {

    /**
     * 查找课程的基本信息
     * 通过courseID查询课程主表并将其转为DTO即可
     * @param courseId
     * @return
     */
    CourseDTO info(Integer courseId);


    /**
     * 新增或修改课程记录
     * @param courseDTO
     * @return
     */
    CourseDTO save(CourseDTO courseDTO);


    /**
     * 根据用户ID查询用户所选修的课程记录
     * 使用userID在Achievement表进行查询获取courseID信息，之后再回Course表查询即可
     * 不需要详细信息
     * @param userId
     * @return
     */
    List<CourseDTO> getCourseByUserId(Integer userId);


    /**
     * 根据课程ID展示课程详情
     * 需要查询主表和副表
     * @param courseId
     * @return
     */
    CourseDTO detail(Integer courseId);


    /**
     * 展示所有的课程主表记录
     * @param userId
     * @return
     */
    List<CourseDTO> list(Integer userId);

    /**
     * 展示所有的课程主表记录(教师端)
     * @param userId
     * @return
     */
    List<CourseDTO> listCourse(Integer userId);


    /**
     * 根据课程ID注销课程
     * @param courseId
     * @return
     */
    boolean invalid(Integer courseId);


    /**
     * 根据课程ID解锁课程
     * @param courseId
     * @return
     */
    boolean unlock(Integer courseId);

    /**
     *根据courseId设置实验为锁定状态
     * @param courseId
     * @return
     */
    boolean lock(Integer courseId);

    /**
     *
     * 根据courseId设置课程为end状态
     * @param courseId
     * @return
     */
    boolean end(Integer courseId);

    /**
     * 更新 课程及其下属实验的上课人数
     * @param courseId
     */
    void updateNumber(Integer courseId);

}
