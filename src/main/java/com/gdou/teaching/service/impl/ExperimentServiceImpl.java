package com.gdou.teaching.service.impl;

import com.gdou.teaching.Enum.AnswerStatusEnum;
import com.gdou.teaching.Enum.ExperimentStatusEnum;
import com.gdou.teaching.Enum.FileCategoryEnum;
import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.dto.AnswerDTO;
import com.gdou.teaching.dto.ExperimentDTO;
import com.gdou.teaching.dto.FileDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.mbg.mapper.*;
import com.gdou.teaching.mbg.model.*;
import com.gdou.teaching.service.AnswerService;
import com.gdou.teaching.service.ExperimentService;
import com.gdou.teaching.service.FileService;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ExperimentServiceImpl implements ExperimentService {
    private final ExperimentMasterMapper experimentMasterMapper;
    private final ExperimentDetailMapper experimentDetailMapper;
    private final FileMapper fileMapper;
    private final FileService fileService;
    private final CourseMasterMapper courseMasterMapper;
    private final ExperimentAnswerMapper answerMapper;
    private final UserReExperimentMapper userReExperimentMapper;
    private final ExperimentAnswerMapper experimentAnswerMapper;
    private final AnswerService answerService;

    public ExperimentServiceImpl(ExperimentMasterMapper experimentMasterMapper, ExperimentDetailMapper experimentDetailMapper, FileMapper fileMapper, FileService fileService, CourseMasterMapper courseMasterMapper, ExperimentAnswerMapper answerMapper, UserReExperimentMapper userReExperimentMapper, ExperimentAnswerMapper experimentAnswerMapper,AnswerService answerService) {
        this.experimentMasterMapper = experimentMasterMapper;
        this.experimentDetailMapper = experimentDetailMapper;
        this.fileMapper = fileMapper;
        this.fileService = fileService;
        this.courseMasterMapper = courseMasterMapper;
        this.answerMapper = answerMapper;
        this.userReExperimentMapper = userReExperimentMapper;
        this.experimentAnswerMapper = experimentAnswerMapper;
        this.answerService = answerService;
    }

    @Value("${caffeine.course.expire-seconds}")
    private int courseExpireSeconds;

    @Value("${caffeine.course.maxSize}")
    private int courseMaxSize;

    @Value("${caffeine.experiment.expire-seconds}")
    private int experimentExpireSeconds;

    @Value("${caffeine.experiment.maxSize}")
    private int experimentMaxSize;

    //Caffeine核心接口：cache,LoadingCache,AsyncLoadingCache
    /**
     * ExperimentDTOLis的缓存
     */
    private LoadingCache<Integer,List<ExperimentDTO>> experimentDTOListCache;
    /**
     * Experiment的缓存
     */
    private LoadingCache<Integer,ExperimentDTO> experimentCache;

    /**
     * 被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。
     */
    @PostConstruct
    public void init(){
        //初始化课程ID对应的实验列表的缓存
        experimentDTOListCache = Caffeine.newBuilder()
                .maximumSize(courseMaxSize)
                .expireAfterWrite(courseExpireSeconds, TimeUnit.SECONDS)
                //在这里的方法可以加入二级缓存
                .build((courseId)-> listFromDB(courseId));
        experimentCache = Caffeine.newBuilder()
                .maximumSize(experimentMaxSize)
                .expireAfterWrite(courseExpireSeconds, TimeUnit.SECONDS)
                //在这里的方法可以加入二级缓存
                .build((experimentId)-> detailFromDB(experimentId));
    }
    @Override
    public ExperimentDTO detail(Integer experimentId){
        return experimentCache.get(experimentId);
    }

    public ExperimentDTO detailFromDB(Integer experimentId) {
        ExperimentDTO experimentDTO = new ExperimentDTO();
        //需要查询的数据有：
        // 主表数据 副表detail数据 实验文件数据
        ExperimentMaster experimentMaster = experimentMasterMapper.selectByPrimaryKey(experimentId);
        if(experimentMaster==null){
            log.info("[ExperimentServiceImpl]-datail查询实验详情信息,实验主表不存在,experimentId={}",experimentId);
            throw new TeachingException(ResultEnum.EXPERIMENT_NOT_EXIST);
        }
        ExperimentDetail experimentDetail = experimentDetailMapper.selectByPrimaryKey(experimentMaster.getExperimentDetailId());
        if(experimentDetail!=null){
            BeanUtils.copyProperties(experimentDetail,experimentDTO);
        }
        List<FileDTO> fileDTOList = fileService.selectFileByCategoryAndFileCategoryId(FileCategoryEnum.EXPERIMENT_FILE.getCode(), experimentId);
        //fileDTOList为null, 则new一个新的List,不为空则设置进去.
        experimentDTO.setExperimentDetailFile(fileDTOList==null?new ArrayList<>():fileDTOList);
        //属性拷贝
        BeanUtils.copyProperties(experimentMaster,experimentDTO);
        return experimentDTO;
    }

    @Override
    @Transactional
    public ExperimentDTO add(ExperimentDTO experimentDTO) {
        ExperimentAnswer experimentAnswer=new ExperimentAnswer();
        ExperimentMaster experimentMaster=new ExperimentMaster();
        ExperimentDetail experimentDetail=new ExperimentDetail();
        //对experimentAnswer进行新增
        BeanUtils.copyProperties(experimentDTO,experimentAnswer);
        //如果存在答案,则添加答案表信息
        if ((experimentAnswer.getExperimentAnswerContent()!=null&&!experimentAnswer.getExperimentAnswerContent().isEmpty())||
                experimentDTO.getExperimentAnswerFile()!=null&& !experimentDTO.getExperimentAnswerFile().isEmpty()){
            experimentAnswer.setExperimentAnswerStatus(AnswerStatusEnum.NORMAL.getCode().byteValue());
            if (experimentAnswerMapper.insert(experimentAnswer)<=0){
                log.error("保存实验,保存实验答案失败,ExperimentAnswer={}",experimentAnswer);
                throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
            }
            //新增实验答案的文件：
            List<FileDTO> experimentAnswerFile = experimentDTO.getExperimentAnswerFile();
            if(experimentAnswerFile!=null&&!experimentAnswerFile.isEmpty()){
                fileService.saveFile(FileCategoryEnum.EXPERIMENT_ANSWER_FILE.getCode(),experimentAnswer.getExperimentAnswerId(),experimentAnswerFile);
            }
        }
        // 先对experimentDetail进行新增
        BeanUtils.copyProperties(experimentDTO,experimentDetail);
        if( experimentDetailMapper.insert(experimentDetail)<=0){
            log.error("[ExperimentServiceImpl]-保存实验,新增实验详情表失败,experimentDetail={}",experimentDetail);
            throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
        }
        //回填ExperimentDetailId,ExperimentAnswerId
        experimentDTO.setExperimentDetailId(experimentDetail.getExperimentDetailId());
        experimentDTO.setExperimentAnswerId(experimentAnswer.getExperimentAnswerId());
        //查询应提交人数
        Integer courseId = experimentDTO.getCourseId();
        CourseMaster courseMaster = courseMasterMapper.selectByPrimaryKey(courseId);
        if(courseMaster==null){
            log.info("[ExperimentServiceImpl]-save 保存实验信息,课程主表不存在,courseId={}",courseId);
            throw new TeachingException(ResultEnum.COURSE_NOT_EXIST);
        }
        experimentDTO.setExperimentParticipationNum(courseMaster.getCourseNumber());
        //检查是否需要加上默认值
        if(experimentDTO.getValve()==null){
            experimentDTO.setValve((float) 0.9);
        }
        if(experimentDTO.getPunishment()==null){
            experimentDTO.setPunishment((float) 0.9);
        }
        if(experimentDTO.getExperimentStatus()==null){
            experimentDTO.setExperimentStatus(ExperimentStatusEnum.NORMAL.getCode().byteValue());
        }
        if (experimentDTO.getExperimentIntro()==null){
            String text=experimentDTO.getExperimentText();
            if (text.length()>15){
                experimentDTO.setExperimentIntro(text.substring(0,15));
            }else{
                experimentDTO.setExperimentIntro(text);
            }
        }
        experimentDTO.setExperimentCommitNum(0);
        BeanUtils.copyProperties(experimentDTO,experimentMaster);
        if (experimentMasterMapper.insert(experimentMaster)<=0){
            log.error("[ExperimentServiceImpl]-保存实验,新增实验主表失败,experimentMaster={}",experimentMaster);
            throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
        }
        //回填ExperimentId
        experimentDTO.setExperimentId(experimentMaster.getExperimentId());
        //新增实验的文件：
        List<FileDTO> experimentDetailFile = experimentDTO.getExperimentDetailFile();
        if(experimentDetailFile!=null&&!experimentDetailFile.isEmpty()){
            fileService.saveFile(FileCategoryEnum.EXPERIMENT_FILE.getCode(),experimentMaster.getExperimentId(),experimentDetailFile);
        }
        return experimentDTO;
    }

    @Override
    public boolean autoUnLock(Integer experimentId) {
        ExperimentMaster experimentMaster = experimentMasterMapper.selectByPrimaryKey(experimentId);
        ExperimentMasterExample experimentMasterExample = new ExperimentMasterExample();
        //取出courseId相同,大于experimentId的实验.
        experimentMasterExample.createCriteria().andCourseIdEqualTo(experimentMaster.getCourseId()).andExperimentIdGreaterThan(experimentId);
        List<ExperimentMaster> experimentMasterList = experimentMasterMapper.selectByExample(experimentMasterExample);
        if(experimentMasterList==null||experimentMasterList.isEmpty()){
            return false;
        }
        Integer target=Integer.MAX_VALUE;
        for(ExperimentMaster experiment:experimentMasterList){
             if (experiment.getExperimentId()<target){
                 target=experiment.getExperimentId();
             }
        }
        try{
            unlock(target);
        }catch (TeachingException e){
            return false;
        }
        return true;
    }
    /**
     * 通过课程ID获取实验列表，以DTO方式返回
     * 此处只查询实验主表即可，用于首页列表显示
     * COMMON方法
     * @param courseId
     * @return
     */
    @Override
    public List<ExperimentDTO> list(Integer courseId) {
//        return listFromDB(courseId);
        return experimentDTOListCache.get(courseId);
    }

    public List<ExperimentDTO> listFromDB(Integer courseId){
        log.info("load list from DB.");
        ExperimentMasterExample experimentMasterExample = new ExperimentMasterExample();
        experimentMasterExample.createCriteria().andCourseIdEqualTo(courseId).andExperimentStatusNotEqualTo(ExperimentStatusEnum.INVALID.getCode().byteValue());
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
    public boolean updateExperimentInfo(ExperimentDTO experimentDTO) {
        ExperimentMaster experimentMaster=new ExperimentMaster();
        BeanUtils.copyProperties(experimentDTO,experimentMaster);
        if (experimentMasterMapper.updateByPrimaryKeySelective(experimentMaster)!=1){
            log.info("[ExperimentServiceImpl]-更新实验基本信息失败");
            throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
        }
        return true;
    }

    @Override
    public boolean updateExperimentDetail(ExperimentDTO experimentDTO) {
        ExperimentDetail experimentDetail=new ExperimentDetail();
        BeanUtils.copyProperties(experimentDTO,experimentDetail);
        if (experimentDetailMapper.updateByPrimaryKeyWithBLOBs(experimentDetail)!=1){
            log.info("[ExperimentServiceImpl]-更新实验详情失败");
            throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
        }
        return true;
    }

    @Override
    public boolean invalid(Integer experimentId) {
        ExperimentMaster experimentMaster =
                experimentMasterMapper.selectByPrimaryKey(experimentId);
        if (experimentMaster == null) {
            log.info("[ExperimentServiceImpl]-删除实验,该实验不存在或已被删除");
            throw new TeachingException(ResultEnum.EXPERIMENT_NOT_EXIST);
        }
        //判断当前状态,仅结束状态的时候可被取消
        if (experimentMaster.getExperimentStatus().intValue()!=ExperimentStatusEnum.END.getCode()){
            log.info("[ExperimentServiceImpl]-完结实验,实验主表状态异常,status={}",experimentMaster.getExperimentStatus());
            throw new TeachingException(ResultEnum.EXPERIMENT_STATUS_ERROR);
        }
        experimentMaster.setExperimentStatus(ExperimentStatusEnum.INVALID.getCode().byteValue());
        if (experimentMasterMapper.updateByPrimaryKeySelective(experimentMaster)!=1){
            log.info("[ExperimentServiceImpl]-删除实验,实验删除失败");
            throw new TeachingException(ResultEnum.EXPERIMENT_INVALID_ERROR);
        }
        return true;
    }

    @Override
    public boolean end(Integer experimentId) {
        ExperimentMaster experimentMaster =
                experimentMasterMapper.selectByPrimaryKey(experimentId);
        if (experimentMaster == null) {
            log.info("[ExperimentServiceImpl]-完结实验,该实验不存在或已被删除");
            throw new TeachingException(ResultEnum.EXPERIMENT_NOT_EXIST);
        }
        //判断当前状态
        if (experimentMaster.getExperimentStatus().intValue()!=(ExperimentStatusEnum.NORMAL.getCode())
                &&experimentMaster.getExperimentStatus().intValue()!=(ExperimentStatusEnum.LOCK.getCode())){
            log.info("[ExperimentServiceImpl]-完结实验,实验主表状态异常,status={}",experimentMaster.getExperimentStatus());
            throw new TeachingException(ResultEnum.EXPERIMENT_STATUS_ERROR);
        }
        experimentMaster.setExperimentStatus(ExperimentStatusEnum.END.getCode().byteValue());
        if (experimentMasterMapper.updateByPrimaryKeySelective(experimentMaster)!=1){
            log.info("[ExperimentServiceImpl]-完结实验,实验完结失败");
            throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
        }
        return true;
    }



    @Override
    public boolean lock(Integer experimentId) {
        ExperimentMaster experimentMaster =
                experimentMasterMapper.selectByPrimaryKey(experimentId);
        if (experimentMaster == null) {
            log.info("[ExperimentServiceImpl]-锁定实验,该实验不存在或已被删除");
            throw new TeachingException(ResultEnum.EXPERIMENT_NOT_EXIST);
        }
        //判断当前状态
        if (experimentMaster.getExperimentStatus().intValue()!=ExperimentStatusEnum.NORMAL.getCode()){
            log.info("[ExperimentServiceImpl]-锁定实验,实验主表状态异常,status={}",experimentMaster.getExperimentStatus());
            throw new TeachingException(ResultEnum.EXPERIMENT_STATUS_ERROR);
        }
        experimentMaster.setExperimentStatus(ExperimentStatusEnum.LOCK.getCode().byteValue());
        if (experimentMasterMapper.updateByPrimaryKeySelective(experimentMaster)!=1){
            log.info("[ExperimentServiceImpl]-锁定实验,实验锁定失败");
            throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
        }
        return true;
    }

    @Override
    public boolean unlock(Integer experimentId) {
        ExperimentMaster experimentMaster =
                experimentMasterMapper.selectByPrimaryKey(experimentId);
        if (experimentMaster == null) {
            log.info("[ExperimentServiceImpl]-解锁实验,该实验不存在或已被删除");
            throw new TeachingException(ResultEnum.EXPERIMENT_NOT_EXIST);
        }
        //判断当前状态
        if (experimentMaster.getExperimentStatus().intValue()!=ExperimentStatusEnum.LOCK.getCode()){
            log.info("[ExperimentServiceImpl]-解锁实验主表,实验主表状态异常,status={}",experimentMaster.getExperimentStatus());
            throw new TeachingException(ResultEnum.EXPERIMENT_STATUS_ERROR);
        }
        experimentMaster.setExperimentStatus(ExperimentStatusEnum.NORMAL.getCode().byteValue());
        if (experimentMasterMapper.updateByPrimaryKeySelective(experimentMaster)!=1){
            log.info("[ExperimentServiceImpl]-解锁实验,实验解锁失败");
            throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
        }
        return true;
    }

    @Override
    public ExperimentMaster updateCommitNumber(Integer experimentId) {
        UserReExperimentExample example = new UserReExperimentExample();
        example.createCriteria().andExperimentIdEqualTo(experimentId);
        int i = userReExperimentMapper.countByExample(example);
        ExperimentMaster experimentMaster = new ExperimentMaster();
        experimentMaster.setExperimentId(experimentId);
        experimentMaster.setExperimentCommitNum(i);
        experimentMasterMapper.updateByPrimaryKeySelective(experimentMaster);
        return experimentMasterMapper.selectByPrimaryKey(experimentId);
    }

    /**
     * 把已有课程下的实验导入新的课程
     * @param oldCourseId 已有的课程ID
     * @param newCourseId 新的课程ID
     * @return
     */
    @Override
    public boolean importExperiment(Integer oldCourseId, Integer newCourseId) {
        // 获取原有课程下的实验列表
        ExperimentMasterExample experimentMasterExample = new ExperimentMasterExample();
        experimentMasterExample.createCriteria().andCourseIdEqualTo(oldCourseId).andExperimentStatusNotEqualTo(ExperimentStatusEnum.INVALID.getCode().byteValue());
        List<ExperimentMaster> experimentMasters = experimentMasterMapper.selectByExample(experimentMasterExample);
        List<ExperimentDTO> experimentDTOList = experimentMasters.stream().map( experimentMaster -> {
            ExperimentDTO detail = detail(experimentMaster.getExperimentId());
            //设置实验内容的初始值
            detail.setCourseId(newCourseId);
            detail.setExperimentId(null);
            detail.setExperimentDetailId(null);
            detail.setExperimentCommitNum(0);
            detail.setCreateTime(null);
            detail.setExperimentStatus(ExperimentStatusEnum.LOCK.getCode().byteValue());
            //查找实验答案和附件
            if(detail.getExperimentAnswerId()!=null){
                AnswerDTO answerDTO = answerService.selectOne(detail.getExperimentAnswerId());
                detail.setExperimentAnswerFile(answerDTO.getExperimentAnswerFileList());
                detail.setExperimentAnswerContent(answerDTO.getExperimentAnswerContent());
            }
            detail.setExperimentAnswerId(null);
            return detail;
        }).collect(Collectors.toList());
        //批量新增实验
        for(ExperimentDTO experimentDTO:experimentDTOList){
            add(experimentDTO);
        }
        return true;
    }
}
