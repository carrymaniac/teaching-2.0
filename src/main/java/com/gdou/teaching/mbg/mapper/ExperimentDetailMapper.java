package com.gdou.teaching.mbg.mapper;

import com.gdou.teaching.mbg.model.ExperimentDetail;
import com.gdou.teaching.mbg.model.ExperimentDetailExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperimentDetailMapper {
    int countByExample(ExperimentDetailExample example);

    int deleteByExample(ExperimentDetailExample example);

    int deleteByPrimaryKey(Integer experimentDetailId);

    int insert(ExperimentDetail record);

    int insertSelective(ExperimentDetail record);

    List<ExperimentDetail> selectByExampleWithBLOBs(ExperimentDetailExample example);

    List<ExperimentDetail> selectByExample(ExperimentDetailExample example);

    ExperimentDetail selectByPrimaryKey(Integer experimentDetailId);

    int updateByExampleSelective(@Param("record") ExperimentDetail record, @Param("example") ExperimentDetailExample example);

    int updateByExampleWithBLOBs(@Param("record") ExperimentDetail record, @Param("example") ExperimentDetailExample example);

    int updateByExample(@Param("record") ExperimentDetail record, @Param("example") ExperimentDetailExample example);

    int updateByPrimaryKeySelective(ExperimentDetail record);

    int updateByPrimaryKeyWithBLOBs(ExperimentDetail record);

    int updateByPrimaryKey(ExperimentDetail record);
}