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

    static final  int B = 1024;
    static final  int KB = 1048576;
    static final  int MB = 1073741824;
    static final String DOC = ".doc";
    static final String DOCX = ".docx";
    static final String PPT = ".ppt";
    static final String PPTX = ".pptx";

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
        switch (suffixName){
            case DOC:
            case DOCX: return "doc";
            case PPT:
            case PPTX: return "ppt";
            case ".xls":
            case ".xlsx": return "excel";
            case ".pdf": return "pdf";
            case ".txt": return "txt";
            case ".png": return "png";
            case ".jpg":
            case ".jpeg": return "jpg";
            default: return "other";
        }

//        if(DOC.equals(suffixName)|| DOCX.equals(suffixName)){
//            return "doc";
//        }
//        if(PPT.equals(suffixName)|| PPTX.equals(suffixName)){
//            return "ppt";
//        }
//        if(".xls".equals(suffixName)|| ".xlsx".equals(suffixName)){
//            return "excel";
//        }
//        if(".pdf".equals(suffixName)){
//            return "pdf";
//        }
//        if(".txt".equals(suffixName)){
//            return "txt";
//        }
//        return "other";
    }



    public String getFileSize(Long fileSize){
        DecimalFormat df = new DecimalFormat("#.00");
        String size = "";
        if (fileSize < B) {
            size = df.format((double) fileSize) + "BT";
        } else if (fileSize < KB) {
            size = df.format((double) fileSize / B) + "KB";
        } else if (fileSize < MB) {
            size = df.format((double) fileSize / KB) + "MB";
        } else {
            size = df.format((double) fileSize / MB) +"GB";
        }
        return size;
    }
}
