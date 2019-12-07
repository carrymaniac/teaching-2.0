package com.gdou.teaching.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author bo
 * @date Created in 18:29 2019/8/19
 * @description
 **/
@Data
public class ExperimentForm {
    //实验编号
    private Integer experimentId;

    //实验名称
    @NotEmpty(message = "实验名称必填")
    private String experimentName;

    //实验状态
    @NotNull(message = "实验状态必填")
    private Integer experimentStatus;

    //实验详情Id
    private Integer experimentDetailId;

    //课程Id
    @NotNull(message = "课程Id必填")
    private Integer courseId;

    //课程Id
    @NotNull(message = "课程阈值必填")
    private Float valve;

    //实验内容
    @NotEmpty(message = "实验内容必填")
    private String experimentText;

    //实验资料文件
    private String experimentDetailFile;
}
