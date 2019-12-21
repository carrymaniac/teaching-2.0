package com.gdou.teaching.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author bo
 * @date Created in 16:20 2019/12/9
 * @description
 **/
@Data
public class JudgeForm {
    @NotNull(message = "用户实验记录id必填")
    private Integer userExperimentId;
    //教师评语
    private String teacherComment;
    @NotNull(message = "审核状态必填")
    private Integer status;
    //实验得分
    private Double experimentAchievement;
}
