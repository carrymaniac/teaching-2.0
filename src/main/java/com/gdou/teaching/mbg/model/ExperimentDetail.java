package com.gdou.teaching.mbg.model;

import java.io.Serializable;
import java.util.Date;

public class ExperimentDetail implements Serializable {
    /**
     * 实验详情ID
     *
     * @mbggenerated
     */
    private Integer experimentDetailId;

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
     * 实验内容文本
     *
     * @mbggenerated
     */
    private String experimentText;

    private static final long serialVersionUID = 1L;

    public Integer getExperimentDetailId() {
        return experimentDetailId;
    }

    public void setExperimentDetailId(Integer experimentDetailId) {
        this.experimentDetailId = experimentDetailId;
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

    public String getExperimentText() {
        return experimentText;
    }

    public void setExperimentText(String experimentText) {
        this.experimentText = experimentText;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", experimentDetailId=").append(experimentDetailId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", experimentText=").append(experimentText);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}