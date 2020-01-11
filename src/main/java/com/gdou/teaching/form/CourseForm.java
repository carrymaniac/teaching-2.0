package com.gdou.teaching.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author bo
 * @date Created in 23:42 2019/8/15
 * @description
 **/
@Data
public class CourseForm {
    /**
     * 课程ID
     */
    private Integer courseId;
    /**
     * 课程代码
     */
    @NotEmpty(message = "课程代码必填")
    private String courseCode;
    /**
     * 课程名称
     */
    @NotEmpty(message = "课程名称必填")
    private String courseName;
    /**
     * 授课老师工号
     */
    @NotNull(message = "授课老师Id必填")
    private Integer teacherId;
    /**
     * 课程详情Id
     */
    private Integer courseDetailId;
    /**
     * 学分
     */
    @NotNull(message = "学分必填")
    private Double courseCredit;
    /**
     * 课程介绍
     */
    @NotEmpty(message = "课程介绍必填")
    private String courseIntroduction;
    /**
     * 课程状态
     */
    private Byte courseStatus;
    /**
     * 课程人数
     */
    private Integer courseNumber;

    /**
     * 增加上课学生id列表,以id列表的形式
     */
    private List<Integer> addStudentIdList;

    /**
     * 课程封面
     *
     *
     */
    private String courseCover;
}
