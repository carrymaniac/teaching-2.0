package com.gdou.teaching.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author bo
 * @date Created in 20:00 2020/1/18
 * @description 课程基本信息更新表单
 **/
@Data
public class CourseInfoUpdateForm {
    /**
     * 课程ID
     */
    @NotNull(message = "课程Id必填")
    private Integer courseId;
    /**
     * 课程详情Id
     */
    @NotNull(message = "课程详情Id必填")
    private Integer courseDetailId;
    /**
     * 课程代码
     */
    private String courseCode;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 学分
     */
    private Double courseCredit;
    /**
     * 课程介绍
     */
    private String courseIntroduction;
    /**
     * 课程封面
     */
    private String courseCover;
}
