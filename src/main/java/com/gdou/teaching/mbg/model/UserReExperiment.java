package com.gdou.teaching.mbg.model;

import java.io.Serializable;
import java.util.Date;

public class UserReExperiment implements Serializable {
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
     * 实验名
     *
     * @mbggenerated
     */
    private String experimentName;

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
     * 修改时间
     *
     * @mbggenerated
     */
    private Date updateTime;

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

    private static final long serialVersionUID = 1L;

    public Integer getUserExperimentId() {
        return userExperimentId;
    }

    public void setUserExperimentId(Integer userExperimentId) {
        this.userExperimentId = userExperimentId;
    }

    public Integer getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(Integer experimentId) {
        this.experimentId = experimentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public Double getExperimentAchievement() {
        return experimentAchievement;
    }

    public void setExperimentAchievement(Double experimentAchievement) {
        this.experimentAchievement = experimentAchievement;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Boolean getHaveCheckAnswer() {
        return haveCheckAnswer;
    }

    public void setHaveCheckAnswer(Boolean haveCheckAnswer) {
        this.haveCheckAnswer = haveCheckAnswer;
    }

    public String getUserExperimentText() {
        return userExperimentText;
    }

    public void setUserExperimentText(String userExperimentText) {
        this.userExperimentText = userExperimentText;
    }

    public String getTeacherComment() {
        return teacherComment;
    }

    public void setTeacherComment(String teacherComment) {
        this.teacherComment = teacherComment;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userExperimentId=").append(userExperimentId);
        sb.append(", experimentId=").append(experimentId);
        sb.append(", userId=").append(userId);
        sb.append(", classId=").append(classId);
        sb.append(", experimentName=").append(experimentName);
        sb.append(", experimentAchievement=").append(experimentAchievement);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", status=").append(status);
        sb.append(", haveCheckAnswer=").append(haveCheckAnswer);
        sb.append(", userExperimentText=").append(userExperimentText);
        sb.append(", teacherComment=").append(teacherComment);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}