package com.gdou.teaching.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.dto
 * @ClassName: FileDTO
 * @Author: carrymaniac
 * @Description: 文件类DTO
 * @Date: 2019/11/7 10:59 上午
 * @Version:
 */
@Data
@NoArgsConstructor
public class FileDTO {
    /**
     * 文件ID
     */
    private Integer fileId;
    /**
     * 文件名
     */
    @NotEmpty
    private String fileName;
    /**
     * 文件的路径
     */
    @NotEmpty
    private String filePath;
    /**
     * 文件格式
     */
    @NotEmpty
    private String fileType;
    /**
     * 文件大小
     */
    @NotEmpty
    private String fileSize;
    /**
     * 文件创建时间
     */
    private Date createTime;

}
