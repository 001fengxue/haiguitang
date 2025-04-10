package com.vix.haiguitang.service;

import com.vix.haiguitang.model.ChatRoom;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;

import java.util.List;
import java.util.Map;

/**
 * 聊天服务
 */
public interface ChatService {

    /**
     * 和ai对话
     * @param roomId
     * @param message
     * @return
     */
    String doChat(long roomId,String message);

    /**
     * 获取聊天室列表
     * @return
     */
    List<ChatRoom> getChatRoomList();

}
