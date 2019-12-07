package com.gdou.teaching.mbg.model;

import java.io.Serializable;
import java.util.Date;

public class Achievement implements Serializable {
    /**
     * 学生成绩ID
     *
     * @mbggenerated
     */
    private Integer achievementId;

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
     * 学生ID
     *
     * @mbggenerated
     */
    private Integer userId;

    /**
     * 学生名字
     *
     * @mbggenerated
     */
    private String userName;

    /**
     * 课程老师ID
     *
     * @mbggenerated
     */
    private Integer teacherId;

    /**
     * 班级ID
     *
     * @mbggenerated
     */
    private Integer classId;

    /**
     * 学生成绩
     *
     * @mbggenerated
     */
    private Double courseAchievement;

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

    private static final long serialVersionUID = 1L;

    public Integer getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(Integer achievementId) {
        this.achievementId = achievementId;
    }

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Double getCourseAchievement() {
        return courseAchievement;
    }

    public void setCourseAchievement(Double courseAchievement) {
        this.courseAchievement = courseAchievement;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", achievementId=").append(achievementId);
        sb.append(", courseId=").append(courseId);
        sb.append(", courseName=").append(courseName);
        sb.append(", userId=").append(userId);
        sb.append(", userName=").append(userName);
        sb.append(", teacherId=").append(teacherId);
        sb.append(", classId=").append(classId);
        sb.append(", courseAchievement=").append(courseAchievement);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}