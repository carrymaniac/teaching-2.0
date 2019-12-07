package com.gdou.teaching.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.text.DecimalFormat;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.constant
 * @ClassName: FileConstant
 * @Author: carrymaniac
 * @Description: 文件上传下载工具类
 * @Date: 2019/9/12 3:02 下午
 * @Version:
 */
@Component
public class FileUtil {

    @Value("${teaching.uploadPath}")
    public String uploadPath;

    public String genUploadPath(){return uploadPath;}

    public static URI getHost(URI uri) {
        URI effectiveURI = null;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (Throwable var4) {
            effectiveURI = null;
        }
        return effectiveURI;
    }

    public String getType(String suffixName){
        if(".doc".equals(suffixName)|| ".docx".equals(suffixName)){
            return "doc";
        }
        if(".ppt".equals(suffixName)|| ".pptx".equals(suffixName)){
            return "ppt";
        }
        if(".xls".equals(suffixName)|| ".xlsx".equals(suffixName)){
            return "excel";
        }
        if(".pdf".equals(suffixName)){
            return "excel";
        }
        if(".txt".equals(suffixName)){
            return "txt";
        }
        return "other";
    }

    public String getFileSize(Long fileSize){
        DecimalFormat df = new DecimalFormat("#.00");
        String size = "";
        if (fileSize < 1024) {
            size = df.format((double) fileSize) + "BT";
        } else if (fileSize < 1048576) {
            size = df.format((double) fileSize / 1024) + "KB";
        } else if (fileSize < 1073741824) {
            size = df.format((double) fileSize / 1048576) + "MB";
        } else {
            size = df.format((double) fileSize / 1073741824) +"GB";
        }
        return size;
    }
}
