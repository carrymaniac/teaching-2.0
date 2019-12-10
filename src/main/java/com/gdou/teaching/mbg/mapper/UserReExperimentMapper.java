package com.gdou.teaching.mbg.mapper;

import com.gdou.teaching.mbg.model.UserReExperiment;
import com.gdou.teaching.mbg.model.UserReExperimentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserReExperimentMapper {
    int countByExample(UserReExperimentExample example);

    int deleteByExample(UserReExperimentExample example);

    int deleteByPrimaryKey(Integer userExperimentId);

    int insert(UserReExperiment record);

    int insertSelective(UserReExperiment record);

    List<UserReExperiment> selectByExampleWithBLOBs(UserReExperimentExample example);

    List<UserReExperiment> selectByExample(UserReExperimentExample example);

    UserReExperiment selectByPrimaryKey(Integer userExperimentId);

    int updateByExampleSelective(@Param("record") UserReExperiment record, @Param("example") UserReExperimentExample example);

    int updateByExampleWithBLOBs(@Param("record") UserReExperiment record, @Param("example") UserReExperimentExample example);

    int updateByExample(@Param("record") UserReExperiment record, @Param("example") UserReExperimentExample example);

    int updateByPrimaryKeySelective(UserReExperiment record);

    int updateByPrimaryKeyWithBLOBs(UserReExperiment record);

    int updateByPrimaryKey(UserReExperiment record);
}