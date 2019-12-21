package com.gdou.teaching.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author bo
 * @date Created in 20:08 2019/12/18
 * @description  批量修改Form表单
 **/
@Data
public class BatchJudgeForm {
    @NotNull(message = "用户实验记录id列表必填")
    private List<Integer> userExperimentIdList;
    //教师评语
    private String teacherComment;

    @NotNull(message = "审核状态必填")
    private Integer status;
    //实验得分
    private Double experimentAchievement;
}
