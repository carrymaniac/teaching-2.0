package com.gdou.teaching.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.IOUtils;
import com.aliyun.oss.model.*;
import com.gdou.teaching.dao.FileDao;
import com.gdou.teaching.dto.FileDTO;
import com.gdou.teaching.mbg.mapper.FileMapper;
import com.gdou.teaching.service.FileService;
import com.gdou.teaching.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author bo
 * @date 2020-05-14 00:44
 * @Description:
 */
@Service
@Primary
@Slf4j
public class FileOSSServiceImpl implements FileService {

    private final FileMapper fileMapper;
    private final FileDao fileDao;
    private final FileUtil fileUtil;

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


    public FileOSSServiceImpl(FileMapper fileMapper, FileDao fileDao, FileUtil fileUtil) {
        this.fileMapper = fileMapper;
        this.fileDao = fileDao;
        this.fileUtil = fileUtil;
    }

    @Override
    public FileDTO selectFileById(Integer fileId) {
        return null;
    }

    @Override
    public List<FileDTO> selectFileByCategoryAndFileCategoryId(Integer fileCategory, Integer fileCategoryId) {
        return null;
    }

    @Override
    public List<FileDTO> selectFileByCategoryAndFileCategoryIdAndKeyword(Integer fileCategory, Integer fileCategoryId, String keyword) {
        return null;
    }

    @Override
    public int saveFile(Integer fileCategory, Integer fileCategoryId, List<FileDTO> FileDTOs) {
        List<com.gdou.teaching.mbg.model.File> files = FileDTOs.stream().map(fileDTO -> {
            com.gdou.teaching.mbg.model.File file = new com.gdou.teaching.mbg.model.File();
            BeanUtils.copyProperties(fileDTO, file);
            file.setFileCategory(fileCategory.byteValue());
            file.setFileCategoryId(fileCategoryId);
            file.setFileStatus((byte)0);
            return file;
        }).collect(Collectors.toList());
        return fileDao.insertList(files);
    }

    @Override
    public boolean deleteFiles(Integer fileCategory, Integer fileCategoryId) {
        return false;
    }

    /**
     * 核心上传功能
     * @Author: Captain&D
     * @cnblogs: https://www.cnblogs.com/captainad
     * @param fileName 文件名
     * @param filePath 文件路径
     * @param inputStream 文件输入流
     * @return
     */
    private String coreUpload(String fileName, String filePath, InputStream inputStream) {
        if(StringUtils.isEmpty(fileName) || inputStream == null) {
            log.error("文件上传时缺少文件名或输入流");
            return null;
        }
        //生成文件路径
//        if(StringUtils.isEmpty(filePath)) {
//            log.warn("File path is lack when upload file but we automatically generated");
//            String dateCategory = DateUtil.getFormatDate(new Date(), "yyyyMMdd");
//            filePath = FLAG_SLANTING_ROD.concat(dateCategory).concat(FLAG_SLANTING_ROD);
//        }
        String fileUrl;
        OSS ossClient = null;
        try{
            // If the upload file size exceeds the limit
            long maxSizeAllowed = getMaximumFileSizeAllowed();
            if(Long.valueOf(inputStream.available()) > maxSizeAllowed) {
                log.error("文件过大,上传失败.");
                return null;
            }
            // Create OSS instance
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 如果bucket不存在, 自动创建新的 bucket
            if (!ossClient.doesBucketExist(bucketName)) {
                log.info("{} 不存在,正在新建 ", bucketName);
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
            log.info("格式化后文件url为 {}", fileUrl);

            // 上传文件
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, inputStream));
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            if(result != null) {
                log.info("上传结果:{}", result.getETag());
                log.info("上传 {} 成功", fileName);
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

    /**
     * 以文件流的方式上传文件
     * @param fileName 文件名称
     * @param filePath 文件路径
     * @param inputStream 文件输入流
     * @return
     */
    public String uploadFile(String fileName, String filePath, InputStream inputStream) {
        return coreUpload(fileName, filePath, inputStream);
    }

    /**
     * 以文件的形式上传文件
     * @param fileName
     * @param filePath
     * @param file
     * @return
     */
    public String uploadFile(String fileName, String filePath, File file) {
        if(file == null) {
            log.warn("File is lack when upload.");
            return null;
        }
        if(StringUtils.isEmpty(fileName)) {
            log.warn("File name is lack when upload file but we automatically generated");
            String uuidFileName = UUID.randomUUID().toString().replace(FLAG_CROSSBAR, FLAG_EMPTY_STRING);
            String fname = file.getName();
            String suffix = fname.substring(fname.lastIndexOf(FLAG_DOT), fname.length());
            fileName = uuidFileName.concat(suffix);
        }
        InputStream inputStream = null;
        String fileUrl = null;
        try{
            inputStream = new FileInputStream(file);
            fileUrl = uploadFile(fileName, filePath, inputStream);
        }catch (Exception e){
            log.error("Upload file error.", e);
        }finally {
            IOUtils.safeClose(inputStream);
        }
        return fileUrl;
    }



    /**
     * 删除文件
     * @param fileId 文件 id
     */
    @Override
    public boolean deleteFile(Integer fileId) {
        OSS ossClient = null;
        try{
            /**
             * http:// bucketname                              images/abc.jpg = key
             * http:// bucketName.oss-cn-shenzhen.aliyuncs.com/images/abc.jpg
             */
            com.gdou.teaching.mbg.model.File file = fileMapper.selectByPrimaryKey(fileId);

            String key = getHostUrl()+file.getFilePath();
            if(log.isDebugEnabled()) {
                log.debug("Delete file key is {}", key);
            }
            ossClient =new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            ossClient.deleteObject(bucketName, key);

        }catch (Exception e){
            log.error("Delete file error.", e);
        } finally {
            if(ossClient != null) {
                ossClient.shutdown();
            }
        }
        //删除数据库记录
        return fileMapper.deleteByPrimaryKey(fileId)!=1;
    }

    public void test() throws Exception{
        //String objectName = "images/login.png";
        String objectName = "images/设计总说明.docx";
        System.out.println(endpoint);
        System.out.println(accessKeyId);
        System.out.println(accessKeySecret);
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元信息。
        OSSObject ossObject = ossClient.getObject(bucketName, objectName);
        // 调用ossObject.getObjectContent获取文件输入流，可读取此输入流获取其内容。
        InputStream content = ossObject.getObjectContent();

        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(fileUtil.uploadPath+"test"));
        
        // 关闭OSSClient。
        ossClient.shutdown();
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
