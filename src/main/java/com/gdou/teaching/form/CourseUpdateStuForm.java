package com.gdou.teaching.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author bo
 * @date Created in 14:53 2019/9/14
 * @description
 **/
@Data
public class CourseUpdateStuForm {
    /**
     * 课程ID
     */
    @NotNull(message = "课程编号必填")
    private Integer courseId;

    /**
     * 增加上课学生列表(userId)
     */

    private List<Integer> addStudentIdList;
    /**
     * 删除上课学生列表(userId)
     */
    private List<Integer> deleteStudentIdList;

}
