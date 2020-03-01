package com.gdou.teaching.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

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
    @Range(min=0, max=100,message = "实验成绩输入错误")
    private Double experimentAchievement;
}
