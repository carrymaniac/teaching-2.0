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
    private Integer classId;
    private Integer userId;
    private String userNumber;
    private String nickName;
    private Double courseAchievement;
}
