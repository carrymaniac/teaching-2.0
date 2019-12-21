package com.gdou.teaching.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gdou.teaching.Enum.RecordStatusEnum;
import lombok.Data;

/**
 * @author bo
 * @date Created in 16:34 2019/12/7
 * @description
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JudgeVO {
    /**
     * 提交记录Id
     */

    private Integer userExperimentId;
    /**
     *  班级Id
     */
    private Integer classId;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 用户Id
     */
    private Integer userId;
    /**
     * 学号
     */
    private String userNumber;
    /**
     * 学生名称
     */
    private String nickName;
    /**
     *  提交状态
     */
    private Integer status;
}
