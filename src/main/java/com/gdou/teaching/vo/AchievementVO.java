package com.gdou.teaching.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author bo
 * @date Created in 17:55 2019/11/29
 * @description
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AchievementVO {
    /**
     * 班级Id
     */
    private Integer classId;
    /**
     * 用户Id
     */
    private Integer userId;
    /**
     * 学号
     */
    private String userNumber;
    /**
     *学生名称
     */
    private String nickName;
    /**
     *成绩
     */
    private Double courseAchievement;
}
