package com.gdou.teaching.mbg.model;

import java.io.Serializable;
import java.util.Date;

public class ExperimentAnswer implements Serializable {
    /**
     * 实验答案ID
     *
     * @mbggenerated
     */
    private Integer experimentAnswerId;

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
     * 实验答案状态，可以用于删除，0表示正常，1表示删除
     *
     * @mbggenerated
     */
    private Byte experimentAnswerStatus;

    /**
     * 实验答案内容
     *
     * @mbggenerated
     */
    private String experimentAnswerContent;

    private static final long serialVersionUID = 1L;

    public Integer getExperimentAnswerId() {
        return experimentAnswerId;
    }

    public void setExperimentAnswerId(Integer experimentAnswerId) {
        this.experimentAnswerId = experimentAnswerId;
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

    public Byte getExperimentAnswerStatus() {
        return experimentAnswerStatus;
    }

    public void setExperimentAnswerStatus(Byte experimentAnswerStatus) {
        this.experimentAnswerStatus = experimentAnswerStatus;
    }

    public String getExperimentAnswerContent() {
        return experimentAnswerContent;
    }

    public void setExperimentAnswerContent(String experimentAnswerContent) {
        this.experimentAnswerContent = experimentAnswerContent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", experimentAnswerId=").append(experimentAnswerId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", experimentAnswerStatus=").append(experimentAnswerStatus);
        sb.append(", experimentAnswerContent=").append(experimentAnswerContent);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}