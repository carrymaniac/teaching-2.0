package com.gdou.teaching.mbg.mapper;

import com.gdou.teaching.mbg.model.ExperimentMaster;
import com.gdou.teaching.mbg.model.ExperimentMasterExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ExperimentMasterMapper {
    int countByExample(ExperimentMasterExample example);

    int deleteByExample(ExperimentMasterExample example);

    int deleteByPrimaryKey(Integer experimentId);

    int insert(ExperimentMaster record);

    int insertSelective(ExperimentMaster record);

    List<ExperimentMaster> selectByExample(ExperimentMasterExample example);

    ExperimentMaster selectByPrimaryKey(Integer experimentId);

    int updateByExampleSelective(@Param("record") ExperimentMaster record, @Param("example") ExperimentMasterExample example);

    int updateByExample(@Param("record") ExperimentMaster record, @Param("example") ExperimentMasterExample example);

    int updateByPrimaryKeySelective(ExperimentMaster record);

    int updateByPrimaryKey(ExperimentMaster record);
}