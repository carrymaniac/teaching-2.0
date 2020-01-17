package com.gdou.teaching.form;

import com.gdou.teaching.mbg.model.User;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author carrymaniac
 * @date Created in 14:46 2019-08-14
 * @description
 **/
@Data
public class ClazzRegisterForm {
    @NotNull
    private String className;
    /**
     * 学院名
     */
    @NotNull
    private String college;
    /**
     * 系名
     */
    @NotNull
    private String series;
    /**
     * 专业名
     */
    @NotNull
    private String major;

    @NotNull
    private List<User> userList;
}
