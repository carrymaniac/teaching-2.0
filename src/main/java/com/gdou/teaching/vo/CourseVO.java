package com.gdou.teaching.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author bo
 * @date Created in 0:40 2019/8/16
 * @description
 **/

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseVO {
    /**
     * 课程编号
     */
    private Integer courseId;
    /**
     * 课程代码
     */
    private String courseCode;
    /**
     *  课程名称
     */
    private String courseName;
    /**
     * 授课老师名
     */
    private String teacherNickname;
    /**
     * 授课老师Id
     */
    private Integer userId;
    /**
     *  课程详情Id
     */
    private Integer courseDetailId;
    /**
     * 学分
     */
    private Double courseCredit;
    /**
     * 课程状态
     */
    private Byte courseStatus;
    /**
     * 课程介绍
     */
    private String courseIntroduction;
    /**
     * 课程班级列表
     */
    private List<ClazzVO> clazzList;
    /**
     * 课程下属实验
     */
    List<ExperimentVO> ExperimentDTOList;
}
