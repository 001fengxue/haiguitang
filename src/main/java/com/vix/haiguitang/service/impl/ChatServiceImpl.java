package com.vix.haiguitang.service.impl;

import com.vix.haiguitang.constant.SystemConstant;
import com.vix.haiguitang.manager.AiManager;
import com.vix.haiguitang.model.ChatRoom;
import com.vix.haiguitang.service.ChatService;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ChatServiceImpl implements ChatService {

    @Resource
    private AiManager aiManager;

    //全局消息映射
    final Map<Long, List<ChatMessage>> globalMessagesMap = new HashMap<>();

    @Override
    public String doChat(long roomId, String message) {
        //系统预设
        final String systemPrompt = SystemConstant.SYSTEM_PROMPT;
        //准备消息列表(关联历史上下文)
        List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(systemPrompt).build();
        final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(message).build();

        //首次开始时,初始化消息列表
        if(!message.equals(SystemConstant.START) && !globalMessagesMap.containsKey(roomId)){
            return "请先输入" + SystemConstant.START + "开启新一轮游戏哦！";
        }

        if (message.equals(SystemConstant.START) && !globalMessagesMap.containsKey(roomId)) {
            globalMessagesMap.put(roomId, messages);
            messages.add(systemMessage);
        } else {
            //非首次
            messages = globalMessagesMap.get(roomId);
        }
        messages.add(userMessage);
        //调用AI
        String answer = aiManager.doChat(messages);
        final ChatMessage assistantMessage = ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content(answer).build();
        messages.add(assistantMessage);

        //3.释放资源
        if(answer.contains(SystemConstant.END)){
            globalMessagesMap.remove(roomId);
        }

        //返回结果
        return answer;
    }

    @Override
    public List<ChatRoom> getChatRoomList() {
        List<ChatRoom> chatRoomList = new ArrayList<>();
        for (Map.Entry<Long, List<ChatMessage>> roomIdListEntry : globalMessagesMap.entrySet()) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setRoomId(roomIdListEntry.getKey());
            chatRoom.setChatMessageList(roomIdListEntry.getValue());
            chatRoomList.add(chatRoom);
        }
        return chatRoomList;
    }


}
