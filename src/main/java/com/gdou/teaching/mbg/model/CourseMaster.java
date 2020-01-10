package com.gdou.teaching.mbg.model;

import java.io.Serializable;
import java.util.Date;

public class CourseMaster implements Serializable {
    /**
     * 课程ID
     *
     * @mbggenerated
     */
    private Integer courseId;

    /**
     * 课程名
     *
     * @mbggenerated
     */
    private String courseName;

    /**
     * 课程教师ID
     *
     * @mbggenerated
     */
    private Integer teacherId;

    /**
     * 课程详情ID
     *
     * @mbggenerated
     */
    private Integer courseDetailId;

    /**
     * 课程状态
     *
     * @mbggenerated
     */
    private Byte courseStatus;

    /**
     * 课程人数
     *
     * @mbggenerated
     */
    private Integer courseNumber;

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
     * 课程封面
     *
     * @mbggenerated
     */
    private String courseCover;

    private static final long serialVersionUID = 1L;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getCourseDetailId() {
        return courseDetailId;
    }

    public void setCourseDetailId(Integer courseDetailId) {
        this.courseDetailId = courseDetailId;
    }

    public Byte getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(Byte courseStatus) {
        this.courseStatus = courseStatus;
    }

    public Integer getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(Integer courseNumber) {
        this.courseNumber = courseNumber;
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

    public String getCourseCover() {
        return courseCover;
    }

    public void setCourseCover(String courseCover) {
        this.courseCover = courseCover;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", courseId=").append(courseId);
        sb.append(", courseName=").append(courseName);
        sb.append(", teacherId=").append(teacherId);
        sb.append(", courseDetailId=").append(courseDetailId);
        sb.append(", courseStatus=").append(courseStatus);
        sb.append(", courseNumber=").append(courseNumber);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", courseCover=").append(courseCover);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}