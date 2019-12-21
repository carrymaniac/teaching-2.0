package com.gdou.teaching.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author carrymaniac
 * @date Created in 22:22 2019-08-14
 * @description user和userInfo和class的结合体
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Integer userId;
    private String nickname;
    private String userNumber;
    private String headUrl;
    private Integer classId;
    private Byte userStatus;
    private Integer userIdent;
    private String mail;
    private String phone;
    private String college;
    private String series;
    private String major;
    private String className;
}
