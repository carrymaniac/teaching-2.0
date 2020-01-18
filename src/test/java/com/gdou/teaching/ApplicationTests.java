package com.gdou.teaching;

import com.gdou.teaching.util.CommonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

    @Test
    void contextLoads() {
        String s = CommonUtil.genConversationId(3, 4);
        System.out.println(s);
        int toId = CommonUtil.getToId("3-4", 3);
        System.out.println(toId);
    }

}
