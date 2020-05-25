package com.gdou.teaching.server;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.server
 * @ClassName: FileServer
 * @Author: carrymaniac
 * @Description: 文件服务接口
 * @Date: 2020/5/19 8:32 下午
 * @Version:
 */
public interface FileServer {
    /**
     * 文件上传
     * @param fileName 文件名
     * @param fileName 文件存储路径
     * @param file
     * @return
     */
    String uploadFile(HttpServletRequest httpServletRequest,String fileName, MultipartFile file);

    /**
     * 核心上传
     * @param fileName
     * @param filePath
     * @param inputStream
     * @return
     */
    String coreUpload(String fileName, String filePath, InputStream inputStream);

    /**
     * 批量删除文件
     * @param list
     */
    void deleteFiles(List<com.gdou.teaching.mbg.model.File> list);

    /**
     * 删除文件
     * @param file
     */
    void deleteFile(com.gdou.teaching.mbg.model.File file);
}
