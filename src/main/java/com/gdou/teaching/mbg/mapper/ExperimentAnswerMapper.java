package com.gdou.teaching.mbg.mapper;

import com.gdou.teaching.mbg.model.ExperimentAnswer;
import com.gdou.teaching.mbg.model.ExperimentAnswerExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ExperimentAnswerMapper {
    int countByExample(ExperimentAnswerExample example);

    int deleteByExample(ExperimentAnswerExample example);

    int deleteByPrimaryKey(Integer experimentAnswerId);

    int insert(ExperimentAnswer record);

    int insertSelective(ExperimentAnswer record);

    List<ExperimentAnswer> selectByExampleWithBLOBs(ExperimentAnswerExample example);

    List<ExperimentAnswer> selectByExample(ExperimentAnswerExample example);

    ExperimentAnswer selectByPrimaryKey(Integer experimentAnswerId);

    int updateByExampleSelective(@Param("record") ExperimentAnswer record, @Param("example") ExperimentAnswerExample example);

    int updateByExampleWithBLOBs(@Param("record") ExperimentAnswer record, @Param("example") ExperimentAnswerExample example);

    int updateByExample(@Param("record") ExperimentAnswer record, @Param("example") ExperimentAnswerExample example);

    int updateByPrimaryKeySelective(ExperimentAnswer record);

    int updateByPrimaryKeyWithBLOBs(ExperimentAnswer record);

    int updateByPrimaryKey(ExperimentAnswer record);
}