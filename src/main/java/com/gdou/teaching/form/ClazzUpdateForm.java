package com.gdou.teaching.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author carrymaniac
 * @date Created in 18:33 2019-08-15
 * @description 班级更新表单 其中包括 删除的用户(学生）班级名称
 **/
@Data
public class ClazzUpdateForm {
    @NotNull
    private String className;
    @NotNull
    private Integer classId;

    private String userListForInsert;

    private String userListForDel;
}
