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
public class ExperimentAnswerUpdateForm {
    //实验Id
    @NotNull(message = "实验Id必填")
    private Integer experimentId;
    //实验答案阈值
    @NotNull(message = "实验答案阈值必填")
    private Float punishment;
    //实验答案Id
    @NotNull(message = "实验答案Id必填")
    private Integer experimentAnswerId;
    //实验答案文本
    private String experimentAnswerContent;

}
