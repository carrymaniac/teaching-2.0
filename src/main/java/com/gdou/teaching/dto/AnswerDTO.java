package com.gdou.teaching.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author bo
 * @date Created in 22:47 2019/10/14
 * @description
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerDTO {
    /**
     * 实验答案ID
     */
    private Integer experimentAnswerId;
    /**
     * 实验答案内容
     */
    private String experimentAnswerContent;
    /**
     * 实验答案文件路径
     */
    private List<FileDTO> experimentAnswerFileList;
    /**
     * 实验ID
     */
    private Integer experimentId;
    /**
     * 实验答案状态，可以用于删除，0表示正常，1表示删除
     */
    private Byte experimentAnswerStatus;
}
