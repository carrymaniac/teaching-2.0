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
    /**
     * 用户提交记录ID
     * - 为空时，说明时一次新增操作
     * - 不为空时，说明是更新操作
     */
    private Integer userExperimentId;
    /**
     * 实验ID
     * - 不为空
     */
    @NotNull(message = "实验id必填")
    private Integer experimentId;
    /**
     * 用户实验提交记录文本内容
     * - 不为空
     */
    @NotEmpty(message = "实验文本内容必填")
    private String userExperimentText;
    /**
     * 用户实验提交记录文件内容
     * - 为空时表示此次提交无文件
     * - 不为空时说明有文件需要提交
     */
    private List<FileDTO> userExperimentFile;
    /**
     * 用户提交状态
     *
     */
    private Integer status;
}
