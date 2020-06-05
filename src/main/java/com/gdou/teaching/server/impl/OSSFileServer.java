package com.gdou.teaching.server.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.IOUtils;
import com.aliyun.oss.model.*;
import com.gdou.teaching.server.FileServer;
import com.gdou.teaching.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.server.impl
 * @ClassName: OSSFileServer
 * @Author: carrymaniac
 * @Description: OSS版本的文件服务
 * @Date: 2020/5/19 8:40 下午
 * @Version:
 */
@Slf4j
public class OSSFileServer implements FileServer {
    /**
     * 斜杠
     */
    private final String FLAG_SLANTING_ROD = "/";
    /**
     * http://
     */
    private final String FLAG_HTTP = "http://";
    /**
     * https://
     */
    private final String FLAG_HTTPS = "https://";
    /**
     * 空字符串
     */
    private final String FLAG_EMPTY_STRING = "";
    /**
     * 点号
     */
    private final String FLAG_DOT = ".";
    /**
     * 横杠
     */
    private final String FLAG_CROSSBAR = "-";
    /**
     * 缺省的最大上传文件大小：300M
     */
    private final int DEFAULT_MAXIMUM_FILE_SIZE = 300;
    /**
     * endpoint
     */
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    /**
     * access key id
     */
    @Value("${aliyun.oss.keyid}")
    private String accessKeyId;
    /**
     * access key secret
     */
    @Value("${aliyun.oss.keysecret}")
    private String accessKeySecret;
    /**
     * bucket name (namespace)
     */
    @Value("${aliyun.oss.bucketname}")
    private String bucketName;
    /**
     * file host (dev/test/prod)
     */
    @Value("${aliyun.oss.filehost}")
    private String fileHost;

    @Autowired
    FileUtil fileUtil;

    @Override
    public String uploadFile(HttpServletRequest httpServletRequest,String fileName, MultipartFile file){
        //文件后缀名字
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String newFileName = sdf.format(new Date())+new Random().nextInt(100)+suffixName;
        String fileDirectory = fileUtil.getPath(suffixName);
        if(fileName == null || file == null ) {
            log.warn("上传文件,文件名或文件不能空");
            return null;
        }
        InputStream inputStream = null;
        String fileUrl = null;
        try{
            inputStream = file.getInputStream();
            fileUrl = coreUpload(newFileName, fileDirectory, inputStream);
        }catch (Exception e){
            log.error("上传文件失败", e);
        }finally {
            IOUtils.safeClose(inputStream);
        }
        return fileUrl;
    }

    @Override
    public String coreUpload(String fileName, String filePath, InputStream inputStream){
        if(StringUtils.isEmpty(fileName) || inputStream == null) {
            log.error("上传文件,缺少文件名或输入流");
            return null;
        }
        String fileUrl;
        OSS ossClient = null;
        try{
            // If the upload file size exceeds the limit
            long maxSizeAllowed = getMaximumFileSizeAllowed();
            if(Long.valueOf(inputStream.available()) > maxSizeAllowed) {
                log.error("上传文件,文件大小超过限制");
                return null;
            }
            // 创建OSS 实例
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 如果bucket不存在, 自动创建新的 bucket
            if (!ossClient.doesBucketExist(bucketName)) {
                log.info("{}不存在,正在新建 ", bucketName);
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                //设置公共读权限
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }

            // 格式化文件存储路径
            if(!filePath.startsWith(FLAG_SLANTING_ROD)) {
                filePath = FLAG_SLANTING_ROD.concat(filePath);
            }
            if(!filePath.endsWith(FLAG_SLANTING_ROD)) {
                filePath = filePath.concat(FLAG_SLANTING_ROD);
            }
            // 格式化文件url
            StringBuilder buffer = new StringBuilder();
            buffer.append(fileHost).append(filePath).append(fileName);
            fileUrl = buffer.toString();
            log.info("格式化后文件url为:{}", fileUrl);

            // 上传文件
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, inputStream));
            if(result != null) {
                log.info("上传结果:{}", result.getETag());
                log.info("上传:{} 成功", fileName);
            }
            fileUrl = getHostUrl().concat(fileUrl);

        }catch (Exception e){
            log.error("上传文件失败", e);
            fileUrl = null;
        }finally {
            if(ossClient != null) {
                ossClient.shutdown();
            }
        }
        return fileUrl;
    }

    @Override
    public void deleteFiles(List<com.gdou.teaching.mbg.model.File> files) {
        OSS ossClient = null;
        try{
            List<String> keys =files.stream().map( file -> file.getFilePath().replace(getHostUrl(),FLAG_EMPTY_STRING)).collect(Collectors.toList());
            ossClient =new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keys));
        }catch (Exception e){
            log.error("删除文件错误", e);
        } finally {
            if(ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Override
    public void deleteFile(com.gdou.teaching.mbg.model.File file) {
        OSS ossClient = null;
        try{
            /**
             * 删除文件。key等同于ObjectName，表示删除OSS文件时需要指定包含文件后缀在内的完整路径，例如images/abc.jpg。
             * http:// bucketname                              images/abc.jpg = key
             * http:// bucketname.oss-cn-shenzhen.aliyuncs.com/images/anbc.jpg
             */
            String key = file.getFilePath();
            key = key.replace(getHostUrl(), FLAG_EMPTY_STRING);
            ossClient =new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            ossClient.deleteObject(bucketName, key);
        }catch (Exception e){
            log.error("删除文件错误", e);
        } finally {
            if(ossClient != null) {
                ossClient.shutdown();
            }
        }

    }

    /**
     * 获取访问的base地址
     * @return
     */
    private String getHostUrl() {
        String hostUrl = null;
        if(this.endpoint.startsWith(FLAG_HTTP)) {
            hostUrl = FLAG_HTTP.concat(this.bucketName).concat(FLAG_DOT)
                    .concat(this.endpoint.replace(FLAG_HTTP, FLAG_EMPTY_STRING)).concat(FLAG_SLANTING_ROD);
        } else if (this.endpoint.startsWith(FLAG_HTTPS)) {
            return FLAG_HTTPS.concat(this.bucketName).concat(FLAG_DOT)
                    .concat(this.endpoint.replace(FLAG_HTTPS, FLAG_EMPTY_STRING)).concat(FLAG_SLANTING_ROD);
        }
        return hostUrl;
    }
    /**
     * 获取最大允许上传文件的大小
     * @return
     */
    private long getMaximumFileSizeAllowed() {
        // 缓存单位是M
        return DEFAULT_MAXIMUM_FILE_SIZE * 1024L * 1024L;
    }
}
