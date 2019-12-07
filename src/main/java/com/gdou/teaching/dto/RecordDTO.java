package com.gdou.teaching.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.relational.core.sql.In;

import java.util.Date;
import java.util.List;

/**
 * @author bo
 * @date Created in 16:41 2019/9/12
 * @description
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecordDTO {
    /**
     * 学生——实验连接表ID
     *
     * @mbggenerated
     */
    private Integer userExperimentId;

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
     * 学生ID
     *
     * @mbggenerated
     */
    private Integer userId;

    /**
     * 班级ID
     *
     * @mbggenerated
     */
    private Integer classId;

    /**
     * 实验成绩
     *
     * @mbggenerated
     */
    private Double experimentAchievement;

    /**
     * 创建时间
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * 学生提交的实验状态。0为已提交，老师未查看打分；1为已打分,通过老师审核；2为已打分，未通过老师审核，需要重新修改上传
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 是否查看过答案，如果查看过则在判分时需要扣分
     *
     * @mbggenerated
     */
    private Boolean haveCheckAnswer;

    /**
     * 实验内容文本
     *
     * @mbggenerated
     */
    private String userExperimentText;

    /**
     * 实验提交的老师点评评语
     *
     * @mbggenerated
     */
    private String teacherComment;

    /**
     * 用户实验提交记录文件内容
     */
    private List<FileDTO> userExperimentFile;


}
