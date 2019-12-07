package com.gdou.teaching.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author carrymaniac
 * @date Created in 17:04 2019-08-09
 * @description 用户注册表单form
 **/
@Data
public class UserRegisterForm {
    @NotNull
    private String userNumber;
    @NotNull
    private String nickname;
    @NotNull
    private String password;
    @NotNull
    private Integer classId;
    @NotNull
    private Integer userIdent;
}
