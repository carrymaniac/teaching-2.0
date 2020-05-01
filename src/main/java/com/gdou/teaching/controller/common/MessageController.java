package com.gdou.teaching.controller.common;

import com.gdou.teaching.Enum.MessageStatusEnum;
import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.dataobject.HostHolder;
import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.mbg.model.Message;
import com.gdou.teaching.service.MessageService;
import com.gdou.teaching.service.UserService;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ConversationVO;
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

    private final UserService userService;
    private final MessageService messageService;
    private final HostHolder hostHolder;

    @Autowired
    public MessageController(MessageService messageService, HostHolder hostHolder, UserService userService) {
        this.messageService = messageService;
        this.hostHolder = hostHolder;
        this.userService = userService;
    }


    @GetMapping("/conversationList")
    @ResponseBody
    ResultVO getLetterList(@RequestParam(value = "page",defaultValue = "1",required = false) Integer page,
                           @RequestParam(value = "size",defaultValue = "10",required = false) Integer size){
        Integer userId = hostHolder.getUser().getUserId();
        List<ConversationVO> conversationList = messageService.getConversationList(userId, page - 1, size);
        //查询普通私信未读消息数量
        int letterUnreadCount = messageService.selectLetterUnreadCount(userId, null);
        //查询系统通知的未读数量
        int noticeUnreadCount = messageService.selectSystemMessageUnreadCount(userId);
        //放置结果
        HashMap<String,Object> result = new HashMap<>(3);
        result.put("conversations",conversationList);
        result.put("letterUnreadCount",letterUnreadCount);
        result.put("noticeUnreadCount",noticeUnreadCount);
        return ResultVOUtil.success(result);
    }

    @GetMapping("/conversation")
    @ResponseBody
    ResultVO getConversation(@RequestParam(value = "page",defaultValue = "1",required = false) Integer page,
                             @RequestParam(value = "size",defaultValue = "10",required = false) Integer size,
                             @RequestParam(value = "conversationId")String conversationId){

        Integer userId = hostHolder.getUser().getUserId();
        PageInfo<Message> conversation = messageService.getConversation(conversationId, page, size);
        /**
         * 理清流程 1.查询拿到所有的消息
         *         2.拿到两个用户的消息，对方的ID 头像 名字
         *         3.拿到消息的内容
         *         4.返回的内容结构为：HashMap{
         *              message:[
         *                  {
         *                      time:
         *                      content:
         *                      fromMe: true
         *                  }
         *              ]
         *              targetUser: user信息
         *              hasNextPage:
         *         }
         */
        if(conversation.getSize()!=0){
            List<Message> list = conversation.getList();
            HashMap<String,Object> map = new HashMap<>(5);
            //分出me you
            List<HashMap<String, Object>> messageList = list.stream()
                    .map(message -> {
                                HashMap<String, Object> obj = new HashMap<>(2);
                                obj.put("time", message.getCreateTime());
                                obj.put("content", message.getContent());
                                obj.put("fromMe",message.getFromId().equals(userId));
                                return obj;
                            }
                    ).collect(Collectors.toList());
            map.put("message",messageList);
            map.put("hasNextPage",conversation.isHasNextPage());

            Message m = list.get(0);
            int targetId = userId.equals(m.getFromId()) ? m.getToId() : m.getFromId();
            //查询目标的头像以及个人信息
            UserDTO userById = userService.selectOne(targetId);
            map.put("targetId",targetId);
            map.put("targetHeadUrl",userById.getHeadUrl());
            map.put("targetName",userById.getNickname());


            //同时设置这些消息中「未读且 toId为自己的即对方发的消息 」的设置为已读
            List<Integer> ids = list.stream().filter(message -> message.getStatus().equals(MessageStatusEnum.UNREAD.getCode())&&message.getToId().equals(userId))
                    .map(message -> message.getMessageId()).collect(Collectors.toList());
            if(!ids.isEmpty()){
                //异步设置已读
                messageService.setMessageRead(ids);
            }
            return ResultVOUtil.success(map);
        }else {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR);
        }
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
    @PostMapping("/sendMessage")
    @ResponseBody
    ResultVO sendMessage(@RequestParam("toId")Integer toId,@RequestParam("content")String content){
        Integer userId = hostHolder.getUser().getUserId();
        Message message = messageService.addMessage(userId, toId, content);
        HashMap<String,Object> result  = new HashMap<>(1);
        result.put("conversationId",message.getConversationId());
        return ResultVOUtil.success(result);
    }

    @PostMapping("/messageRead")
    @ResponseBody
    ResultVO messageRead(@RequestParam("messageIdList")List<Integer> messageIdList){
        if(!messageIdList.isEmpty()){
            messageService.setMessageRead(messageIdList);
        }
        return ResultVOUtil.success();
    }



}
