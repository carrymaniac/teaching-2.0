package com.gdou.teaching.service;


import com.gdou.teaching.dto.CourseDTO;
import com.gdou.teaching.dto.ExperimentDTO;
import com.gdou.teaching.dto.FileDTO;
import com.gdou.teaching.exception.TeachingException;

import java.util.HashMap;
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
    CourseDTO info(Integer courseId) throws TeachingException;


    /**
     * 新增或修改课程记录
     * @param courseDTO
     * @return
     */
    CourseDTO save(CourseDTO courseDTO);



    /**
     * 根据课程ID展示课程详情
     * 需要查询主表和副表
     * @param courseId
     * @return
     */
    CourseDTO detail(Integer courseId) throws TeachingException;


    /**
     * 展示学生的所有的选修的课程主表记录
     * 不需要副表
     * @param userId 学生端
     * @return
     */
    List<CourseDTO> listCourseForStudent(Integer userId);

    /**
     * 展示所有的课程主表记录(教师端)
     * @param userId 教师ID
     * @return
     */
    List<CourseDTO> listCourseForTeacher(Integer userId);

    /**
     * 展示对应教师的所有的课程主表记录+副表记录（用于15字介绍）(管理员端)
     * @param userId 教师ID
     * @return
     */
    HashMap<String,Object> listCourseForAdminByTeacherIdAndKeywordInPage(Integer userId, Integer page, Integer size, String keyWord, Integer status);

    /**
     * 展示对应学生所选修的所有的课程主表记录+副表记录（用于15字介绍）(管理员端)
     * @param userId 学生ID
     * @return
     */
    HashMap<String,Object> listCourseForAdminByStudentIdAndKeywordAndStatusInPage(Integer page, Integer size,Integer userId,String keyword,Integer status);

    /**
     * 增加课程资源文件
     * @param courseId
     * @param courseFile
     * @return
     */
    boolean addCourseFile(Integer courseId, List<FileDTO> courseFile);

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
