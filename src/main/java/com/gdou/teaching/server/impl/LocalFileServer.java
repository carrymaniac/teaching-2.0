package com.gdou.teaching.server.impl;

import com.gdou.teaching.server.FileServer;
import com.gdou.teaching.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.server.impl
 * @ClassName: LocalFileServer
 * @Author: carrymaniac
 * @Description: 本地文件存储
 * @Date: 2020/5/19 8:47 下午
 * @Version:
 */

@Slf4j
public class LocalFileServer implements FileServer {
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    FileUtil fileUtil;

    @Override
    public String uploadFile(HttpServletRequest httpServletRequest,String fileName, MultipartFile file) {
        //文件后缀名字
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        //文件存储目录
        File fileDirectory = new File(fileUtil.genUploadPath());
        File destFile = new File(fileUtil.genUploadPath() + newFileName);
        if (!fileDirectory.exists()) {
            if (!fileDirectory.mkdir()) {
                 log.error("文件夹创建失败,路径为：{}" + fileDirectory);
                 return null;
            }
        }
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            log.error("文件上传失败，异常为: {}",e);
            return null;
        }
        String url;
        try {
            url = FileUtil.getHost(new URI(httpServletRequest.getRequestURL() + "")) + contextPath + "/download/" + fileName;
        } catch (URISyntaxException e) {
            log.error("文件上传失败，异常为: {}",e);
            return null;
        }
        return url;
    }

    @Override
    public String coreUpload(String fileName, String filePath, InputStream inputStream) {
        return null;
    }

    @Override
    public void deleteFiles(List<com.gdou.teaching.mbg.model.File> list) {

    }

    @Override
    public void deleteFile(com.gdou.teaching.mbg.model.File file) {

    }
}
