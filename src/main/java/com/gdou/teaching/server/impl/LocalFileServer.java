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


@Slf4j
public class LocalFileServer implements FileServer {
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    FileUtil fileUtil;
    private com.gdou.teaching.mbg.model.File FileName;

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
            url = FileUtil.getHost(new URI(httpServletRequest.getRequestURL() + "")) + contextPath + "/download/" + newFileName;
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
        if(list==null || list.size() == 0){
            return ;
        }
        for(com.gdou.teaching.mbg.model.File file:list){
            deleteFile(file);
        }
    }

    @Override
    public void deleteFile(com.gdou.teaching.mbg.model.File file) {
        if(file == null){
            return ;
        }
        //从文件路径上获取文件名
        String path= file.getFilePath();
        path= path.substring(path.lastIndexOf('/')+1);
        path= fileUtil.genUploadPath().concat(path);
        //读取文件
        File localFile = new File(path);
        if ( !localFile.exists() ){
            log.error("文件删除失败，文件不存在");
        }else{
            localFile.delete();
        }
    }
}
