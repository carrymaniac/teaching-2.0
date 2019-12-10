package com.gdou.teaching.service.impl;

import com.gdou.teaching.dao.FileDao;
import com.gdou.teaching.dto.FileDTO;
import com.gdou.teaching.mbg.mapper.FileMapper;
import com.gdou.teaching.mbg.model.File;
import com.gdou.teaching.mbg.model.FileExample;
import com.gdou.teaching.service.FileService;
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
public class FileServiceImpl implements FileService {
    @Autowired
    FileMapper fileMapper;
    @Autowired
    FileDao fileDao;
    @Override
    public FileDTO selectFileById(Integer fileId) {
        File file = fileMapper.selectByPrimaryKey(fileId);
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
        List<File> files = fileMapper.selectByExample(fileExample);
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
    public List<FileDTO> selectFileByCategoryAndFileCategoryIdAndKeyword(Integer fileCategory, Integer fileCategoryId, String keyword) {
        FileExample fileExample = new FileExample();
        fileExample.createCriteria().andFileCategoryEqualTo(fileCategory.byteValue()).andFileCategoryIdEqualTo(fileCategoryId).andFileNameLike(keyword);
        List<File> files = fileMapper.selectByExample(fileExample);
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
    public int saveFile(Integer fileCategory, Integer fileCategoryId, List<FileDTO> FileDTOs) {
        List<File> files = FileDTOs.stream().map(fileDTO -> {
            File file = new File();
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
        FileExample fileExample = new FileExample();
        fileExample.createCriteria().andFileCategoryEqualTo(fileCategory.byteValue()).andFileCategoryIdEqualTo(fileCategoryId);
        return fileMapper.deleteByExample(fileExample)>0;
    }
}
