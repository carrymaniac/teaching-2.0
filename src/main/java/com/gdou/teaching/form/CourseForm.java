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
     * 课程id
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
     * 增加上课学生id列表,以id列表的形式
     */
    @NotEmpty(message = "班级必填")
    private List<String> addStudentIdList;

    /**
     * 课程封面
     */
    private String courseCover;
}
