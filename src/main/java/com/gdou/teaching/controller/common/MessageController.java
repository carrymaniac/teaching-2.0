package com.gdou.teaching.controller.common;

import com.gdou.teaching.Enum.MessageStatusEnum;
import com.gdou.teaching.dataobject.HostHolder;
import com.gdou.teaching.mbg.model.Message;
import com.gdou.teaching.service.MessageService;
import com.gdou.teaching.util.CommonUtil;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ResultVO;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    private final MessageService messageService;
    private final HostHolder hostHolder;

    @Autowired
    public MessageController(MessageService messageService, HostHolder hostHolder) {
        this.messageService = messageService;
        this.hostHolder = hostHolder;
    }


    @GetMapping("/conversationList")
    @ResponseBody
    ResultVO getLetterList(@RequestParam(value = "page",defaultValue = "1",required = false) Integer page, @RequestParam(value = "size",defaultValue = "10",required = false) Integer size){
        Integer userId = hostHolder.getUser().getUserId();
        List<Message> conversationList = messageService.getConversationList(userId, page - 1, size);
        List<HashMap> messageFromSystem = messageService.getMessageFromSystem(userId);
        HashMap<String,Object> result = new HashMap();
        result.put("conversationList",conversationList);
        //差设置未读数

        return ResultVOUtil.success(result);
    }

    @GetMapping("/conversation")
    @ResponseBody
    ResultVO getConversation(@RequestParam(value = "page",defaultValue = "1",required = false) Integer page,
                             @RequestParam(value = "size",defaultValue = "10",required = false) Integer size,
                             @RequestParam(value = "conversationId",required = true)String conversationId){
        Integer userId = hostHolder.getUser().getUserId();
        PageInfo<Message> conversation = messageService.getConversation(conversationId, page, size);
        List<Message> list = conversation.getList();
        //同时设置这些消息中「未读且 toId为自己的即对方发的消息 」的设置为已读
        List<Integer> ids = list.stream().filter(message -> message.getStatus().equals(MessageStatusEnum.UNREAD.getCode())&&message.getToId().equals(userId))
                .map(message -> message.getMessageId()).collect(Collectors.toList());
        //异步设置已读
        messageService.setMessageRead(ids);
        HashMap<String,Object> map = new HashMap<>();
        map.put("hasNextPage",conversation.isHasNextPage());
        map.put("message",list);
        return ResultVOUtil.success(map);
    }

    /**
     * 获取系统消息
     * @return
     */
    @GetMapping("/systemMessage")
    @ResponseBody
    ResultVO getSystemMessage(){
        Integer userId = hostHolder.getUser().getUserId();
        List<HashMap> messageFromSystem = messageService.getMessageFromSystem(userId);
        return ResultVOUtil.success(messageFromSystem);
    }

    /**
     * 发送一条消息
     * @param toId
     * @param content
     * @return
     */
    @PostMapping("/systemMessage")
    @ResponseBody
    ResultVO sendMessage(@RequestParam("toId")Integer toId,@RequestParam("content")String content){
        Integer userId = hostHolder.getUser().getUserId();
        Message message = messageService.addMessage(userId, toId, content);
        return ResultVOUtil.success(message);
    }





}
