package com.gdou.teaching.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.vo
 * @ClassName: ExperimentVO
 * @Author: carrymaniac
 * @Description: 实验VO类
 * @Date: 2019/9/20 5:49 下午
 * @Version:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExperimentVO {
    //实验ID
    private Integer experimentId;
    //实验名称
    private String experimentName;
    //实验详情ID
    private Integer experimentDetailId;
    //课程ID
    private Integer courseId;
    //实验文本内容
    private String experiment_intro;
    //实验附件文档
    private String experimentDetailFile;
    //提交状态
    private Integer recordStatus;
    //提交人数
    private Integer experimentCommitNum;
    @JsonProperty("time")
    private Date createTime;
}
