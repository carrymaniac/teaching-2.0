package com.gdou.teaching.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author bo
 * @date Created in 22:17 2019/8/18
 * @description
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExperimentDTO {
    /**
     * 实验ID
     *
     * @mbggenerated
     */
    private Integer experimentId;
    /**
     * 实验名
     *
     * @mbggenerated
     */
    private String experimentName;

    /**
     * 实验详情ID
     *
     * @mbggenerated
     */
    private Integer experimentDetailId;

    /**
     * 实验所属课程ID
     *
     * @mbggenerated
     */
    private Integer courseId;
    /**
     * 实验答案ID
     *
     * @mbggenerated
     */
    private Integer experimentAnswerId;
    /**
     * 实验简介
     *
     * @mbggenerated
     */
    private String experimentIntro;
    /**
     * 实验目前提交人数
     *
     * @mbggenerated
     */
    private Integer experimentCommitNum;

    /**
     * 实验参与人数,通过课程人数获得
     *
     * @mbggenerated
     */
    private Integer experimentParticipationNum;

    /**
     * 阈值
     *
     * @mbggenerated
     */
    private Float valve;

    /**
     * 查看答案的惩罚折扣大小
     *
     * @mbggenerated
     */
    private Float punishment;
    /**
     * 创建时间
     *
     * @mbggenerated
     */
    private Date createTime;
    /**
     * 实验状态
     *
     * @mbggenerated
     */
    private Byte experimentStatus;
    /**
     * 实验附件文档
     */
    private List<FileDTO> experimentDetailFile;
    /**
     * 实验内容文本
     *
     * @mbggenerated
     */
    private String experimentText;
    /**
     * 实验答案内容
     *
     * @mbggenerated
     */
    private String experimentAnswerContent;
    /**
     * 实验附件文档
     */
    private List<FileDTO> experimentAnswerFile;

}
