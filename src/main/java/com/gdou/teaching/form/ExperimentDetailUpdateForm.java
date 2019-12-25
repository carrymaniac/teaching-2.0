package com.gdou.teaching.form;

import com.gdou.teaching.dto.FileDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author bo
 * @date Created in 18:29 2019/8/19
 * @description
 **/
@Data
public class ExperimentDetailUpdateForm {
    //课程详情Id
    @NotNull(message = "课程详情Id必填")
    private Integer experimentDetailId;

    //实验内容
    @NotEmpty(message = "实验内容必填")
    private String experimentText;

}
