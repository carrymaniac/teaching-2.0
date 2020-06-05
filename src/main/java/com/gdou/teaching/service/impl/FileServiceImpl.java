package com.gdou.teaching.service.impl;

import com.aliyun.oss.OSS;
import com.gdou.teaching.dao.FileDao;
import com.gdou.teaching.dto.FileDTO;
import com.gdou.teaching.mbg.mapper.FileMapper;
import com.gdou.teaching.mbg.model.File;
import com.gdou.teaching.mbg.model.FileExample;
import com.gdou.teaching.server.FileServer;
import com.gdou.teaching.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.service.impl
 * @ClassName: FileServiceImpl
 * @Author: carrymaniac
 * @Description:
 * @Date: 2019/12/6 9:48 上午
 * @Version:
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;
    private final FileDao fileDao;
    private final FileServer fileServer;



    public FileServiceImpl(FileMapper fileMapper, FileDao fileDao, FileServer fileServer) {
        this.fileMapper = fileMapper;
        this.fileDao = fileDao;
        this.fileServer = fileServer;
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
     *  删除多个文件 (不再删除实际文件)
     * @param fileCategory
     * @param fileCategoryId
     * @return
     */
    @Override
    public boolean deleteFiles(Integer fileCategory, Integer fileCategoryId) {
        FileExample fileExample = new FileExample();
        //获取相关文件
        fileExample.createCriteria().andFileCategoryEqualTo(fileCategory.byteValue()).andFileCategoryIdEqualTo(fileCategoryId);
//        try{
//            List<com.gdou.teaching.mbg.model.File> files = fileMapper.selectByExample(fileExample);
//            fileServer.deleteFiles(files);
//        }catch (Exception e){
//            log.error("删除文件错误", e);
//        }
        //删除数据库的记录
        return fileMapper.deleteByExample(fileExample)>0;
    }


    /**
     * 删除文件 (不再删除实际文件)
     * @param fileId 文件 id
     */
    @Override
    public boolean deleteFile(Integer fileId) {
//        try{
//            com.gdou.teaching.mbg.model.File file = fileMapper.selectByPrimaryKey(fileId);
//            fileServer.deleteFile(file);
//        }catch (Exception e){
//            log.error("删除文件错误", e);
//        }
        //删除数据库记录
        return fileMapper.deleteByPrimaryKey(fileId)!=1;
    }
}

