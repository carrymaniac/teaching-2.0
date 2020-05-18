package com.gdou.teaching.mbg.mapper;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.gdou.teaching.Enum.MessageStatusEnum;
import com.gdou.teaching.mbg.model.Message;
import com.gdou.teaching.mbg.model.MessageExample;
import com.gdou.teaching.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.gdou.teaching.constant.CommonConstant.SYSTEM_USER_ID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.mbg.mapper
 * @ClassName: MessageMapperTest
 * @Author: carrymaniac
 * @Description:
 * @Date: 2020/5/18 5:04 下午
 * @Version:
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class MessageMapperTest {
    @Autowired
    MessageMapper messageMapper;

    @Test
    void selectMessage(){
        MessageExample example = new MessageExample();
        String s = CommonUtil.genConversationId(SYSTEM_USER_ID, 71);
        example.createCriteria().andFromIdEqualTo(SYSTEM_USER_ID).andToIdEqualTo(71).andStatusEqualTo(MessageStatusEnum.UNREAD.getCode());
        List<Message> messages = messageMapper.selectByExample(example);
        log.info("{}", JSONUtil.toJsonPrettyStr(messages));
    }

}