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
    //课程编号
    private Integer courseId;
    //课程代码
    @NotEmpty(message = "课程代码必填")
    private String courseCode;
    //课程名称
    @NotEmpty(message = "课程名称必填")
    private String courseName;
    //授课老师工号
    @NotNull(message = "授课老师Id必填")
    private Integer userId;
    //上课时间
    @NotEmpty(message = "上课时间必填")
    private String showTime;
    //课程详情Id
    private Integer courseDetailId;
    //学分
    @NotNull(message = "学分必填")
    private Double courseCredit;
    //课程介绍
    @NotEmpty(message = "课程介绍必填")
    private String courseIntroduction;

    //增加上课班级列表
    private List<Integer> addClazzIdList;

    //增加上课学生列表(学号)
    private List<String> addStudentIdList;

}
