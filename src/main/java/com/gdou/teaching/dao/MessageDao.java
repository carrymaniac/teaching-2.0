package com.gdou.teaching.dao;

import com.gdou.teaching.mbg.model.Message;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.dao
 * @ClassName: MessageDao
 * @Author: carrymaniac
 * @Description: 自定义MessageDao
 * @Date: 2020/1/18 4:48 下午
 * @Version:
 */
@Repository
public interface MessageDao {

    /**
     * 查询当前用户的会话列表,针对每个会话只返回一条最新的私信.
     * @param userId 用户ID
     * @param offset 分页参数-第几页
     * @param limit 分页参数-每页数量
     * @return
     */
    List<Message> selectConversations(int userId, int offset, int limit);

}
