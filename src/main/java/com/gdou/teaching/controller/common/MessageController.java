package com.gdou.teaching.controller.common;

import com.gdou.teaching.dataobject.HostHolder;
import com.gdou.teaching.mbg.model.Message;
import com.gdou.teaching.service.MessageService;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.controller.common
 * @ClassName: MessageController
 * @Author: carrymaniac
 * @Description: 消息控制器
 * @Date: 2020/1/18 4:42 下午
 * @Version:
 */
@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    MessageService messageService;
    @Autowired
    HostHolder hostHolder;


    @RequestMapping("/list")
    @GetMapping
    @ResponseBody
    ResultVO getLetterList(@RequestParam(value = "page",defaultValue = "1",required = false) Integer page, @RequestParam(value = "size",defaultValue = "10",required = false) Integer size){
        Integer userId = hostHolder.getUser().getUserId();
        List<Message> conversationList = messageService.getConversationList(1, page - 1, size);
        List<HashMap> messageFromSystem = messageService.getMessageFromSystem(1);
        HashMap<String,Object> result = new HashMap();
        result.put("ConverSationList",conversationList);
        result.put("messageFromSystem",messageFromSystem);
        return ResultVOUtil.success(result);
    }

}
