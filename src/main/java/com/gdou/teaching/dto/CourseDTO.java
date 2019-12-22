package com.gdou.teaching.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gdou.teaching.vo.ClazzVO;
import lombok.Data;

import java.util.List;

/**
 * @author bo
 * @date Created in 17:57 2019/8/14
 * @description
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDTO {
    /**
     * 课程编号
     */
    private Integer courseId;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 授课老师Id
     */
    private Integer teacherId;
    /**
     * 授课老师名
     */
    private String teacherNickname;
    /**
     * 课程人数
     */
    private Integer courseNumber;
    /**
     * 课程详情Id
     */
    private Integer courseDetailId;
    /**
     * 课程状态
     */
    private Byte courseStatus;

    /**
     * 学分
     */
    private Double courseCredit;
    /**
     * 课程介绍
     */
    private String courseIntroduction;
    /**
     * 课程代码
     */
    private String courseCode;

    /**
     *  课程班级列表
     */

    private List<ClazzVO> clazzList;
}
