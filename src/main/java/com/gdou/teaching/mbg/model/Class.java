package com.gdou.teaching.mbg.model;

import java.io.Serializable;
import java.util.Date;

public class Class implements Serializable {
    /**
     * 所属班级ID
     *
     * @mbggenerated
     */
    private Integer classId;

    /**
     * 班级名
     *
     * @mbggenerated
     */
    private String className;

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
     * 班级状态
     *
     * @mbggenerated
     */
    private Byte classStatus;

    /**
     * 班级人数
     *
     * @mbggenerated
     */
    private Integer classSize;

    private static final long serialVersionUID = 1L;

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public Byte getClassStatus() {
        return classStatus;
    }

    public void setClassStatus(Byte classStatus) {
        this.classStatus = classStatus;
    }

    public Integer getClassSize() {
        return classSize;
    }

    public void setClassSize(Integer classSize) {
        this.classSize = classSize;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", classId=").append(classId);
        sb.append(", className=").append(className);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", classStatus=").append(classStatus);
        sb.append(", classSize=").append(classSize);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}