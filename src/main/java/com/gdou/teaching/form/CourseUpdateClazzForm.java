package com.gdou.teaching.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author bo
 * @date Created in 22:05 2019/9/14
 * @description
 **/
@Data
public class CourseUpdateClazzForm {
    @NotNull(message = "课程编号必填")
    //课程编号
    private Integer courseId;

    //增加上课班级列表
    private List<Integer> addClazzIdList;

    //删除上课班级列表
    private List<String> deleteClazzIdList;
}
