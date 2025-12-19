package com.jendo.app.domain.chatbot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ChatRequest {
    @NotBlank(message = "Message is required")
    private String message;
    
    private List<ChatHistoryItem> history;
    
    @Data
    public static class ChatHistoryItem {
        private String role;
        private String content;
    }
}
