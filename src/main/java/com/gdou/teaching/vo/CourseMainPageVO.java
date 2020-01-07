package com.gdou.teaching.vo;

import lombok.Data;

import java.util.List;

/**
 * @author bo
 * @date Created in 0:40 2019/8/16
 * @description 课程主页VO
 **/

@Data
public class CourseMainPageVO {
    /**
     * 课程编号
     */
    private Integer courseId;
    /**
     *  课程名称
     */
    private String courseName;

    /**
     *  课程详情Id
     */
    private Integer courseDetailId;
    /**
     * 课程状态
     */
    private Byte courseStatus;

    /**
     * 课程下属实验
     */
    List<ExperimentVO> ExperimentDTOList;
}
