package com.gdou.teaching.form;

import com.gdou.teaching.dto.FileDTO;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author bo
 * @date Created in 18:29 2019/8/19
 * @description
 **/
@Data
public class ExperimentForm {
    //实验名称
    @NotEmpty(message = "实验名称必填")
    private String experimentName;

    //课程Id
    @NotNull(message = "课程Id必填")
    private Integer courseId;

    //实验阈值
    @NotNull(message = "实验阈值必填")
    @Range(min=0, max=1,message = "实验阈值输入错误")
    private Float valve;

    //实验简介
    private String experimentIntro;
    //实验内容
    @NotEmpty(message = "实验内容必填")
    private String experimentText;
    //实验状态
    private Byte experimentStatus;

    //实验答案文本
    private String experimentAnswerContent;

    //查看答案的惩罚折扣大小
    @NotNull(message = "答案阈值必填")
    @Range(min=0, max=1,message = "答案阈值输入错误")
    private Float punishment;
    /**
     * 实验附件文档
     */
    private List<FileDTO> experimentDetailFile;

    /**
     * 实验答案文档
     */
    private List<FileDTO> experimentAnswerFile;
}
