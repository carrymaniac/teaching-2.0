package com.gdou.teaching.dao;

import com.gdou.teaching.mbg.model.File;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.dao
 * @ClassName: FileDao
 * @Author: carrymaniac
 * @Description: File文件自定义Dao类
 * @Date: 2019/12/6 9:56 上午
 * @Version:
 */
@Repository
public interface FileDao {
    /**
     * 批量插入File类对象
     * @param files
     * @return
     */
    public int insertList(List<File> files);
}
