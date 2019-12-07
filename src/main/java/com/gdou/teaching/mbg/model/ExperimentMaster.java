package com.gdou.teaching.mbg.model;

import java.io.Serializable;
import java.util.Date;

public class ExperimentMaster implements Serializable {
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
     * 实验简介
     *
     * @mbggenerated
     */
    private String experimentIntro;

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
    private Integer experimentParticipationNumber;

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
     * 修改时间
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * 实验状态
     *
     * @mbggenerated
     */
    private Byte experimentStatus;

    private static final long serialVersionUID = 1L;

    public Integer getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(Integer experimentId) {
        this.experimentId = experimentId;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public String getExperimentIntro() {
        return experimentIntro;
    }

    public void setExperimentIntro(String experimentIntro) {
        this.experimentIntro = experimentIntro;
    }

    public Integer getExperimentDetailId() {
        return experimentDetailId;
    }

    public void setExperimentDetailId(Integer experimentDetailId) {
        this.experimentDetailId = experimentDetailId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getExperimentAnswerId() {
        return experimentAnswerId;
    }

    public void setExperimentAnswerId(Integer experimentAnswerId) {
        this.experimentAnswerId = experimentAnswerId;
    }

    public Integer getExperimentCommitNum() {
        return experimentCommitNum;
    }

    public void setExperimentCommitNum(Integer experimentCommitNum) {
        this.experimentCommitNum = experimentCommitNum;
    }

    public Integer getExperimentParticipationNumber() {
        return experimentParticipationNumber;
    }

    public void setExperimentParticipationNumber(Integer experimentParticipationNumber) {
        this.experimentParticipationNumber = experimentParticipationNumber;
    }

    public Float getValve() {
        return valve;
    }

    public void setValve(Float valve) {
        this.valve = valve;
    }

    public Float getPunishment() {
        return punishment;
    }

    public void setPunishment(Float punishment) {
        this.punishment = punishment;
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

    public Byte getExperimentStatus() {
        return experimentStatus;
    }

    public void setExperimentStatus(Byte experimentStatus) {
        this.experimentStatus = experimentStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", experimentId=").append(experimentId);
        sb.append(", experimentName=").append(experimentName);
        sb.append(", experimentIntro=").append(experimentIntro);
        sb.append(", experimentDetailId=").append(experimentDetailId);
        sb.append(", courseId=").append(courseId);
        sb.append(", experimentAnswerId=").append(experimentAnswerId);
        sb.append(", experimentCommitNum=").append(experimentCommitNum);
        sb.append(", experimentParticipationNumber=").append(experimentParticipationNumber);
        sb.append(", valve=").append(valve);
        sb.append(", punishment=").append(punishment);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", experimentStatus=").append(experimentStatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}