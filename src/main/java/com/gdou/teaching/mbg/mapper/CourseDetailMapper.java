package com.gdou.teaching.mbg.mapper;

import com.gdou.teaching.mbg.model.CourseDetail;
import com.gdou.teaching.mbg.model.CourseDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CourseDetailMapper {
    int countByExample(CourseDetailExample example);

    int deleteByExample(CourseDetailExample example);

    int deleteByPrimaryKey(Integer courseDetailId);

    int insert(CourseDetail record);

    int insertSelective(CourseDetail record);

    List<CourseDetail> selectByExampleWithBLOBs(CourseDetailExample example);

    List<CourseDetail> selectByExample(CourseDetailExample example);

    CourseDetail selectByPrimaryKey(Integer courseDetailId);

    int updateByExampleSelective(@Param("record") CourseDetail record, @Param("example") CourseDetailExample example);

    int updateByExampleWithBLOBs(@Param("record") CourseDetail record, @Param("example") CourseDetailExample example);

    int updateByExample(@Param("record") CourseDetail record, @Param("example") CourseDetailExample example);

    int updateByPrimaryKeySelective(CourseDetail record);

    int updateByPrimaryKeyWithBLOBs(CourseDetail record);

    int updateByPrimaryKey(CourseDetail record);
}