package com.gdou.teaching.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.IOUtils;
import com.aliyun.oss.model.*;
import com.gdou.teaching.dao.FileDao;
import com.gdou.teaching.dto.FileDTO;
import com.gdou.teaching.mbg.mapper.FileMapper;
import com.gdou.teaching.mbg.model.FileExample;
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
import java.util.ArrayList;
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


    public FileOSSServiceImpl(FileMapper fileMapper, FileDao fileDao) {
        this.fileMapper = fileMapper;
        this.fileDao = fileDao;
    }

    @Override
    public FileDTO selectFileById(Integer fileId) {
        com.gdou.teaching.mbg.model.File file = fileMapper.selectByPrimaryKey(fileId);
        if(file==null){
            return null;
        }
        FileDTO fileDTO = new FileDTO();
        BeanUtils.copyProperties(file,fileDTO);
        return fileDTO;
    }

    @Override
    public List<FileDTO> selectFileByCategoryAndFileCategoryId(Integer fileCategory, Integer fileCategoryId) {
        FileExample fileExample = new FileExample();
        fileExample.createCriteria().andFileCategoryEqualTo(fileCategory.byteValue()).andFileCategoryIdEqualTo(fileCategoryId);
        return getFileDTOS(fileExample);
    }

    private List<FileDTO> getFileDTOS(FileExample fileExample) {
        List<com.gdou.teaching.mbg.model.File> files = fileMapper.selectByExample(fileExample);
        if(files==null||files.isEmpty()){
            return null;
        }
        List<FileDTO> FileDTOs = files.stream().map(file -> {
            FileDTO fileDTO = new FileDTO();
            BeanUtils.copyProperties(file, fileDTO);
            return fileDTO;
        }).collect(Collectors.toList());
        return FileDTOs;
    }

    @Override
    public List<FileDTO> selectFileByCategoryAndFileCategoryIdAndKeyword(Integer fileCategory,Integer fileCategoryId,String keyword) {
        FileExample fileExample = new FileExample();
        fileExample.createCriteria().andFileCategoryEqualTo(fileCategory.byteValue())
                .andFileCategoryIdEqualTo(fileCategoryId).andFileNameLike("%"+keyword+"%");
        return getFileDTOS(fileExample);
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


    /**
     * 核心上传功能
     * @param fileName 文件名
     * @param filePath 文件路径
     * @param inputStream 文件输入流
     * @return
     */
    private String coreUpload(String fileName, String filePath, InputStream inputStream) {
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
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
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
        if(fileName == null || file == null ) {
            log.warn("上传文件,文件名或文件不能空");
            return null;
        }
        InputStream inputStream = null;
        String fileUrl = null;
        try{
            inputStream = new FileInputStream(file);
            fileUrl = uploadFile(fileName, filePath, inputStream);
        }catch (Exception e){
            log.error("上传文件失败", e);
        }finally {
            IOUtils.safeClose(inputStream);
        }
        return fileUrl;
    }

    /**
     *  删除多个文件
     * @param fileCategory
     * @param fileCategoryId
     * @return
     */
    @Override
    public boolean deleteFiles(Integer fileCategory, Integer fileCategoryId) {
        FileExample fileExample = new FileExample();
        OSS ossClient = null;
        try{
            //获取相关文件
            fileExample.createCriteria().andFileCategoryEqualTo(fileCategory.byteValue()).andFileCategoryIdEqualTo(fileCategoryId);
            List<com.gdou.teaching.mbg.model.File> files = fileMapper.selectByExample(fileExample);
            // 格式化文件路径
            List<String> keys =files.stream().map( file -> {
                return file.getFilePath().replace(getHostUrl(),FLAG_EMPTY_STRING);
            }).collect(Collectors.toList());

            ossClient =new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keys));
        }catch (Exception e){
            log.error("删除文件错误", e);
        } finally {
            if(ossClient != null) {
                ossClient.shutdown();
            }
        }
        //删除数据库的记录
        return fileMapper.deleteByExample(fileExample)>0;
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
             * 删除文件。key等同于ObjectName，表示删除OSS文件时需要指定包含文件后缀在内的完整路径，例如images/abc.jpg。
             * http:// bucketname                              images/abc.jpg = key
             * http:// bucketname.oss-cn-shenzhen.aliyuncs.com/images/anbc.jpg
             */
            com.gdou.teaching.mbg.model.File file = fileMapper.selectByPrimaryKey(fileId);
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
        //删除数据库记录
        return fileMapper.deleteByPrimaryKey(fileId)!=1;
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
