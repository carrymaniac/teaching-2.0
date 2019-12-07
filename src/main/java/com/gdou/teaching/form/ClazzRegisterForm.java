package com.gdou.teaching.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author carrymaniac
 * @date Created in 14:46 2019-08-14
 * @description
 **/
@Data
public class ClazzRegisterForm {
    @NotNull
    private String className;
    @NotNull
    private String userList;

}
