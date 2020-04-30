package com.gdou.teaching.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gdou.teaching.dto.FileDTO;
import lombok.Data;

import java.util.List;

/**
 * @author bo
 * @date Created in 14:26 2019/12/9
 * @description
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecordVO {
    /**
     * 用户实验提交记录id
     */
    private Integer userExperimentId;
    /**
     *  用户id
     */
    private Integer userId;
    /**
     *  学生名称
     */
    private String userName;
    /**
     *  学生学号
     */
    private String userNumber;
    /**
     *  用户实验提交记录文本内容
     */
    private String userExperimentText;
    /**
     * 用户头像
     */
    private String headUrl;
    /**
     * 用户实验提交记录文件内容
     */
    private List<FileDTO> userExperimentFile;
    /**
     * 用户实验得分
     */
    private Double experimentAchievement;
    /**
     * 老师评语
     */

    private String teacherComment;
    /**
     *  实验提交状态
     */
    private Byte status;
}
