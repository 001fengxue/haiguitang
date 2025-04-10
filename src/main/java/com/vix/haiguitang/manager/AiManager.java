package com.vix.haiguitang.manager;

import cn.hutool.core.collection.CollUtil;
import com.volcengine.ark.runtime.model.completion.chat.*;
import com.volcengine.ark.runtime.service.ArkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * AI调用管理类
 */
@Service
@Slf4j
public class AiManager {

    @Resource
    private ArkService arkService;

    /**
     * 与AI对话,只允许传入系统预设和用户预设
     * @param systemPrompt
     * @param userPrompt
     * @return
     */
    public String doChat(String systemPrompt, String userPrompt) {
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(systemPrompt).build();
        final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(userPrompt).build();
        messages.add(systemMessage);
        messages.add(userMessage);
        return doChat(messages);
    }

    /**
     * 更通用的对话方法，可以传入多个对话列表
     * @param chatMessageList
     * @return
     */
    public String doChat(List<ChatMessage> chatMessageList) {
//        单次调用
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                // 指定您创建的方舟推理接入点 ID，此处已帮您修改为您的推理接入点 ID
                .model("ep-20250322103350-5g8wx")
                .messages(chatMessageList)
                .build();

        List<ChatCompletionChoice> choiceList = arkService.createChatCompletion(chatCompletionRequest).getChoices();
        if(CollUtil.isEmpty(choiceList)){
            throw new RuntimeException("AI返回结果为空");
        }
        String content = choiceList.get(0).getMessage().getContent().toString();
        log.info("AI返回结果：" + content);
        return content;
    }
}
