package com.gdou.teaching.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author bo
 * @date Created in 21:07 2019/11/18
 * @description
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AchievementDTO {
    /**
     * 成绩ID
     */
    private Integer achievementId;
    /**
     * 课程ID
     */
    private Integer courseId;
    /**
     * 课程名
     */
    private String courseName;
    /**
     * 用户(学生)ID
     */
    private Integer userId;
    /**
     * 用户（学生）名
     */
    private String userName;
    /**
     * 班级ID
     */
    private Integer classId;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 教师ID
     */
    private Integer teacherId;
    /**
     * 教师ID
     */
    private String teacherName;
    /**
     * 成绩
     */
    private Double courseAchievement;
}
