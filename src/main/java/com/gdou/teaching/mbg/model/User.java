package com.gdou.teaching.mbg.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private Integer userId;

    /**
     * 用户名
     *
     * @mbggenerated
     */
    private String nickname;

    /**
     * 用户的工号or学号
     *
     * @mbggenerated
     */
    private String userNumber;

    /**
     * 用户密码
     *
     * @mbggenerated
     */
    private String password;

    /**
     * 用户头像
     *
     * @mbggenerated
     */
    private String headUrl;

    /**
     * 用户密码盐值
     *
     * @mbggenerated
     */
    private String salt;

    /**
     * 班级ID
     *
     * @mbggenerated
     */
    private Integer classId;

    /**
     * 用户状态，默认为0即正常，有1为被禁用，2为注销
     *
     * @mbggenerated
     */
    private Byte userStatus;

    /**
     * 用户身份，默认0为学生，1为老师，2为管理员
     *
     * @mbggenerated
     */
    private Byte userIdent;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Byte getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Byte userStatus) {
        this.userStatus = userStatus;
    }

    public Byte getUserIdent() {
        return userIdent;
    }

    public void setUserIdent(Byte userIdent) {
        this.userIdent = userIdent;
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
        sb.append(", userId=").append(userId);
        sb.append(", nickname=").append(nickname);
        sb.append(", userNumber=").append(userNumber);
        sb.append(", password=").append(password);
        sb.append(", headUrl=").append(headUrl);
        sb.append(", salt=").append(salt);
        sb.append(", classId=").append(classId);
        sb.append(", userStatus=").append(userStatus);
        sb.append(", userIdent=").append(userIdent);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}