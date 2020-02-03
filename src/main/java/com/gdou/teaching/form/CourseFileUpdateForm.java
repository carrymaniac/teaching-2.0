package com.gdou.teaching.form;

import com.gdou.teaching.dto.FileDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author bo
 * @date Created in 22:41 2020/2/1
 * @description
 **/
@Data
public class CourseFileUpdateForm {
    //课程Id
    @NotNull(message = "课程Id必填")
    private Integer courseId;

    /**
     * 课程文件
     */
    @NotNull(message = "课程文件必填")
    private List<FileDTO> courseFile;
}
