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
public class CourseVO {
    /**
     * 课程编号
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer courseId;
    /**
     * 课程代码
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String courseCode;
    /**
     *  课程名称
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String courseName;
    /**
     * 授课老师名
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String teacherNickname;
    /**
     * 授课老师Id
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer userId;
    /**
     *  课程详情Id
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer courseDetailId;
    /**
     * 学分
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double courseCredit;
    /**
     * 课程状态
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Byte courseStatus;
    /**
     * 课程介绍
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String courseIntroduction;
    /**
     * 课程班级列表
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ClazzVO> clazzList;
    /**
     * 课程封面
     *
     * @mbggenerated
     */
    private String courseCover;
    /**
     * 课程下属实验  移动至CourseMainPageVO
     */
    //@JsonInclude(JsonInclude.Include.NON_NULL)
    //List<ExperimentVO> ExperimentDTOList;
}
