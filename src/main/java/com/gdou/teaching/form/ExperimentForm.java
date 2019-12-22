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


    //实验详情Id
    private Integer experimentDetailId;

    //课程Id
    @NotNull(message = "课程Id必填")
    private Integer courseId;

    //课程Id
    @NotNull(message = "课程阈值必填")
    private Float valve;

    //实验简介
    private String experimentIntro;
    //实验内容
    @NotEmpty(message = "实验内容必填")
    private String experimentText;

    //实验答案Id
    private Integer experimentAnswerId;

    //实验答案文本
    private String experimentAnswerContent;

    //查看答案的惩罚折扣大小
    @NotNull(message = "惩罚折扣必填")
    private Float punishment;
}
