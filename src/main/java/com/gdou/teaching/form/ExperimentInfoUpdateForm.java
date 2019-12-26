package com.gdou.teaching.form;

import com.gdou.teaching.dto.FileDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author bo
 * @date Created in 18:29 2019/8/19
 * @description
 **/
@Data
public class  ExperimentInfoUpdateForm {
    //实验编号
    @NotNull(message = "实验Id必填")
    private Integer experimentId;

    //实验名称
    @NotEmpty(message = "实验名称必填")
    private String experimentName;

    //实验简介
    private String experimentIntro;

    //课程阈值
    @NotNull(message = "课程阈值必填")
    private Float valve;

    //查看答案的惩罚折扣大小
    @NotNull(message = "惩罚折扣必填")
    private Float punishment;

}
