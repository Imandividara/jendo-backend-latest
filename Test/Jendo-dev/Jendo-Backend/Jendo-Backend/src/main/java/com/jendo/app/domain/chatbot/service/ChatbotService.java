package com.jendo.app.domain.chatbot.service;

import com.jendo.app.domain.chatbot.dto.ChatRequest;
import com.jendo.app.domain.chatbot.dto.ChatResponse;

public interface ChatbotService {
    ChatResponse sendMessage(ChatRequest request);
}
