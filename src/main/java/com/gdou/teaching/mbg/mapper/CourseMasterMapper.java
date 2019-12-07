package com.gdou.teaching.mbg.mapper;

import com.gdou.teaching.mbg.model.CourseMaster;
import com.gdou.teaching.mbg.model.CourseMasterExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseMasterMapper {
    int countByExample(CourseMasterExample example);

    int deleteByExample(CourseMasterExample example);

    int deleteByPrimaryKey(Integer courseId);

    int insert(CourseMaster record);

    int insertSelective(CourseMaster record);

    List<CourseMaster> selectByExample(CourseMasterExample example);

    CourseMaster selectByPrimaryKey(Integer courseId);

    int updateByExampleSelective(@Param("record") CourseMaster record, @Param("example") CourseMasterExample example);

    int updateByExample(@Param("record") CourseMaster record, @Param("example") CourseMasterExample example);

    int updateByPrimaryKeySelective(CourseMaster record);

    int updateByPrimaryKey(CourseMaster record);
}