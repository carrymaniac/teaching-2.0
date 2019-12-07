package com.gdou.teaching.mbg.model;

import java.io.Serializable;
import java.util.Date;

public class File implements Serializable {
    /**
     * 文件表ID
     *
     * @mbggenerated
     */
    private Integer fileId;

    /**
     * 0-课程文件，1-实验文件，2-实验答案文件，3-学生答案文件
     *
     * @mbggenerated
     */
    private Byte fileCategory;

    /**
     * 文件关联的类别的ID
     *
     * @mbggenerated
     */
    private Integer fileCategoryId;

    /**
     * 文件名
     *
     * @mbggenerated
     */
    private String fileName;

    /**
     * 文件的路径
     *
     * @mbggenerated
     */
    private String filePath;

    /**
     * 文件格式
     *
     * @mbggenerated
     */
    private String fileType;

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
     * 文件状态，默认为0-正常，1-被删除
     *
     * @mbggenerated
     */
    private Byte fileStatus;

    /**
     * 文件大小
     *
     * @mbggenerated
     */
    private String fileSize;

    private static final long serialVersionUID = 1L;

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Byte getFileCategory() {
        return fileCategory;
    }

    public void setFileCategory(Byte fileCategory) {
        this.fileCategory = fileCategory;
    }

    public Integer getFileCategoryId() {
        return fileCategoryId;
    }

    public void setFileCategoryId(Integer fileCategoryId) {
        this.fileCategoryId = fileCategoryId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
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

    public Byte getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(Byte fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", fileId=").append(fileId);
        sb.append(", fileCategory=").append(fileCategory);
        sb.append(", fileCategoryId=").append(fileCategoryId);
        sb.append(", fileName=").append(fileName);
        sb.append(", filePath=").append(filePath);
        sb.append(", fileType=").append(fileType);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", fileStatus=").append(fileStatus);
        sb.append(", fileSize=").append(fileSize);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}