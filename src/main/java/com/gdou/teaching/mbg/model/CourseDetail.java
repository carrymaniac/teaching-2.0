package com.gdou.teaching.mbg.model;

import java.io.Serializable;
import java.util.Date;

public class CourseDetail implements Serializable {
    /**
     * 课程详情ID
     *
     * @mbggenerated
     */
    private Integer courseDetailId;

    /**
     * 课程名
     *
     * @mbggenerated
     */
    private String courseName;

    /**
     * 课程代码
     *
     * @mbggenerated
     */
    private String courseCode;

    /**
     * 课程学分
     *
     * @mbggenerated
     */
    private Long courseCredit;

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
     * 课程介绍
     *
     * @mbggenerated
     */
    private String courseIntroduction;

    private static final long serialVersionUID = 1L;

    public Integer getCourseDetailId() {
        return courseDetailId;
    }

    public void setCourseDetailId(Integer courseDetailId) {
        this.courseDetailId = courseDetailId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Long getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(Long courseCredit) {
        this.courseCredit = courseCredit;
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

    public String getCourseIntroduction() {
        return courseIntroduction;
    }

    public void setCourseIntroduction(String courseIntroduction) {
        this.courseIntroduction = courseIntroduction;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", courseDetailId=").append(courseDetailId);
        sb.append(", courseName=").append(courseName);
        sb.append(", courseCode=").append(courseCode);
        sb.append(", courseCredit=").append(courseCredit);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", courseIntroduction=").append(courseIntroduction);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}