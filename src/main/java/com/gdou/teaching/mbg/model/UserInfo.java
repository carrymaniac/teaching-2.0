package com.gdou.teaching.mbg.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

public class UserInfo implements Serializable {
    private Integer userInfoId;

    private Integer userId;

    /**
     * 电子邮箱
     *
     * @mbggenerated
     */
    private String mail;

    /**
     * 联系电话
     *
     * @mbggenerated
     */
    private String phone;

    /**
     * 学院
     *
     * @mbggenerated
     */
    private String college;

    /**
     * 系
     *
     * @mbggenerated
     */
    private String series;

    /**
     * 专业
     *
     * @mbggenerated
     */
    private String major;

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

    public Integer getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Integer userInfoId) {
        this.userInfoId = userInfoId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
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
        sb.append(", userInfoId=").append(userInfoId);
        sb.append(", userId=").append(userId);
        sb.append(", mail=").append(mail);
        sb.append(", phone=").append(phone);
        sb.append(", college=").append(college);
        sb.append(", series=").append(series);
        sb.append(", major=").append(major);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}