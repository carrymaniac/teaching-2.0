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
     * 根据用户ID查询课程记录
     * @param userId
     * @return
     */
    List<CourseDTO> getCourseByUserId(Integer userId);

    /**
     * 根据课程ID展示课程详情
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
     * 根据课程ID注销课程
     * @param courseId
     * @return
     */
    boolean invalid(Integer courseId);

    /**
     * 根据课程ID恢复课程
     * @param courseId
     * @return
     */
    boolean restore(Integer courseId);

    



}
