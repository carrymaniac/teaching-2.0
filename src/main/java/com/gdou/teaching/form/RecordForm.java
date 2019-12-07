package com.gdou.teaching.form;

import com.gdou.teaching.dto.FileDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author bo
 * @date Created in 21:22 2019/9/12
 * @description
 **/
@Data
public class RecordForm {
    //用户实验提交记录
    private Integer userExperimentId;
    //实验ID
    @NotNull(message = "实验id必填")
    private Integer experimentId;
    //用户实验提交记录文本内容
    @NotEmpty(message = "实验文本内容必填")
    private String userExperimentText;
    //用户实验提交记录文件内容
    private List<FileDTO> userExperimentFile;
    //实验提交状态
    private Integer status;
}
