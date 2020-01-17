package com.gdou.teaching.form;

import com.gdou.teaching.dto.FileDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author bo
 * @date Created in 18:29 2019/8/19
 * @description
 **/
@Data
public class ExperimentFileUpdateForm {
    //实验Id
    @NotNull(message = "实验Id必填")
    private Integer experimentId;

    /**
     * 实验附件文件
     */
    @NotNull(message = "实验附件文件必填")
    private List<FileDTO> experimentDetailFile;

}
