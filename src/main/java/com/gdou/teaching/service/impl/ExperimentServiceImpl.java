package com.gdou.teaching.service.impl;

import com.gdou.teaching.Enum.FileCategoryEnum;
import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.dto.ExperimentDTO;
import com.gdou.teaching.dto.FileDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.mbg.mapper.ExperimentDetailMapper;
import com.gdou.teaching.mbg.mapper.ExperimentMasterMapper;
import com.gdou.teaching.mbg.mapper.FileMapper;
import com.gdou.teaching.mbg.model.ExperimentDetail;
import com.gdou.teaching.mbg.model.ExperimentMaster;
import com.gdou.teaching.mbg.model.ExperimentMasterExample;
import com.gdou.teaching.service.ExperimentService;
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
 * @ClassName: ExperimentServiceImpl
 * @Author: carrymaniac
 * @Description: 实验Service实现类
 * @Date: 2019/12/6 9:43 上午
 * @Version:
 */
@Slf4j
@Service
public class ExperimentServiceImpl implements ExperimentService {
    @Autowired
    ExperimentMasterMapper experimentMasterMapper;
    @Autowired
    ExperimentDetailMapper experimentDetailMapper;
    @Autowired
    FileMapper fileMapper;
    @Autowired
    FileService fileService;
    @Override
    public ExperimentDTO detail(Integer experimentId) {
        ExperimentMaster experimentMaster = experimentMasterMapper.selectByPrimaryKey(experimentId);
        if(experimentMaster==null){
            throw new TeachingException(ResultEnum.EXPERIMENT_NOT_EXIST);
        }
        ExperimentDetail experimentDetail = experimentDetailMapper.selectByPrimaryKey(experimentMaster.getExperimentDetailId());
        if(experimentDetail==null){
            throw new TeachingException(ResultEnum.EXPERIMENT_DETAIL_NOT_EXIST);
        }
        ExperimentDTO experimentDTO = new ExperimentDTO();
        List<FileDTO> fileDTOList = fileService.selectFileByCategoryAndFileCategoryId(FileCategoryEnum.EXPERIMENT_FILE.getCode(), experimentId);
        experimentDTO.setExperimentDetailFile(fileDTOList);
        //属性拷贝
        BeanUtils.copyProperties(experimentDetail,experimentDTO);
        BeanUtils.copyProperties(experimentMaster,experimentDTO);
        return experimentDTO;
    }

    @Override
    public ExperimentDTO save(ExperimentDTO experimentDTO) {
        ExperimentMaster experimentMaster=new ExperimentMaster();
        ExperimentDetail experimentDetail=new ExperimentDetail();
        // 先对experimentDetail进行新增/更新
        BeanUtils.copyProperties(experimentDTO,experimentDetail);
        if (experimentDetail.getExperimentDetailId()==null){
            int insert = experimentDetailMapper.insert(experimentDetail);
            if(insert<=0){
                log.error("保存实验,新增实验详情表失败,experimentDetail={}",experimentDetail);
                throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
            }
        }else{
            int i = experimentDetailMapper.updateByPrimaryKey(experimentDetail);
            if(i<=0){
                log.error("保存实验,更新实验详情表失败,experimentDetail={}",experimentDetail);
                throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
            }
        }

        //回填ExperimentDetailId
        experimentDTO.setExperimentDetailId(experimentDetail.getExperimentDetailId());
        BeanUtils.copyProperties(experimentDTO,experimentMaster);
        if (experimentMaster.getExperimentId()==null){
            Integer i = experimentMasterMapper.insert(experimentMaster);
            if (i<=0){
                log.error("保存实验,新增实验主表失败,experimentMaster={}",experimentMaster);
                throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
            }
        }else{
            int i = experimentMasterMapper.updateByPrimaryKey(experimentMaster);
            if (i<=0){
                log.error("保存实验,更新实验主表失败,experimentMaster={}",experimentMaster);
                throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
            }
        }

        //回填ExperimentId
        experimentDTO.setExperimentId(experimentMaster.getExperimentId());
        return experimentDTO;
    }
    @Override
    public List<ExperimentDTO> list(Integer courseId) {
        ExperimentMasterExample experimentMasterExample = new ExperimentMasterExample();
        experimentMasterExample.createCriteria().andCourseIdEqualTo(courseId);
        List<ExperimentMaster> experimentMasters = experimentMasterMapper.selectByExample(experimentMasterExample);
        if(experimentMasters==null||experimentMasters.isEmpty()){
            return null;
        }
        List<ExperimentDTO> experimentDTOList = experimentMasters.stream().map(experimentMaster -> {
            ExperimentDTO experimentDTO = new ExperimentDTO();
            BeanUtils.copyProperties(experimentMaster,experimentDTO);
            return experimentDTO;
        }).collect(Collectors.toList());
        return experimentDTOList;
    }



    @Override
    public boolean invalid(Integer experimentId) {
        return false;
    }

    @Override
    public ExperimentMaster ban(Integer experimentId) {
        return null;
    }

    @Override
    public ExperimentMaster restore(Integer experimentId) {
        return null;
    }

    @Override
    public ExperimentMaster lock(Integer experimentId) {
        return null;
    }

    @Override
    public ExperimentMaster unlock(Integer experimentId) {
        return null;
    }
}
