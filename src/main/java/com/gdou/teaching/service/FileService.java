package com.gdou.teaching.service;

import com.gdou.teaching.dto.FileDTO;

import java.util.List;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.service
 * @ClassName: FileService
 * @Author: carrymaniac
 * @Description: 文件service
 * @Date: 2019/11/7 10:58 上午
 * @Version:
 */

public interface FileService {
    /**
     * 通过ID选择文件
     * @param fileId 文件ID
     * @return
     */
    FileDTO selectFileById(Integer fileId);

    /**
     * 通过文件关联类别和类别ID查询文件
     * @param fileCategory
     * @param fileCategoryId
     * @return 符合条件的文件
     */
    List<FileDTO> selectFileByCategoryAndFileCategoryId(Integer fileCategory, Integer fileCategoryId);

    /**
     * 通过文件关联类别和类别ID查询文件
     * @param fileCategory
     * @param fileCategoryId
     * @param keyword
     * @return
     */
    List<FileDTO> selectFileByCategoryAndFileCategoryIdAndKeyword(Integer fileCategory, Integer fileCategoryId, String keyword);

    /**
     * 增加
     * @param fileCategory
     * @param fileCategoryId
     * @param FileDTOs 文件DTO
     * @return
     */
    int saveFile(Integer fileCategory, Integer fileCategoryId, List<FileDTO> FileDTOs);

    /**
     * 删除文件
     * @param fileCategory
     * @param fileCategoryId
     * @return
     */
    int deleteFiles(Integer fileCategory, Integer fileCategoryId);

}
