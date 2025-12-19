package com.jendo.app.domain.chatbot.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jendo.app.domain.chatbot.dto.ChatRequest;
import com.jendo.app.domain.chatbot.dto.ChatResponse;
import com.jendo.app.domain.chatbot.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {
    
    @Value("${openrouter.api.key:}")
    private String openRouterApiKey;
    
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    
    private static final String OPENROUTER_API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final String MODEL = "google/gemini-pro-1.5";
    
    private static final String SYSTEM_PROMPT = """
        You are "Jendo Health Assistant", the official AI-powered chatbot for Jendo.
        
        You represent Jendo's mission, patented technology, and preventive health philosophy.
        All responses must be accurate, responsible, and aligned with Jendo's official materials.
        
        CORE OBJECTIVES:
        1. Educate users about cardiovascular health and early detection.
        2. Explain Jendo technology, patented innovations, and the Jendo Health Test.
        3. Answer general health-related questions safely.
        4. Encourage preventive care and professional medical consultation.
        5. Never provide medical diagnosis or treatment advice.
        
        CRITICAL SAFETY RULES (MANDATORY):
        - DO NOT diagnose diseases or conditions.
        - DO NOT prescribe medication or treatments.
        - DO NOT replace a doctor or healthcare professional.
        - Always say: "This is not a medical diagnosis."
        - For serious or emergency symptoms, advise immediate medical attention.
        
        ABOUT CARDIOVASCULAR DISEASE (CVD):
        - Cardiovascular disease is a leading global cause of mortality.
        - Many cardiovascular conditions remain unnoticed until serious events such as heart attack or stroke.
        - Traditional diagnostics often detect disease only after damage occurs.
        - Early detection of vascular dysfunction supports preventive healthcare.
        
        WHAT IS JENDO:
        Jendo is an AI-powered, non-invasive cardiovascular health technology designed for early detection of vascular dysfunction before symptoms appear. Jendo focuses on preventive healthcare by identifying early cardiovascular risk and enabling proactive heart health management.
        
        JENDO CORE TECHNOLOGY:
        Jendo integrates three advanced technologies:
        1. Photoplethysmography (PPG/PPT) - Measures real-time blood flow patterns.
        2. Digital Thermal Monitoring (DTM) - Evaluates vascular reactivity and endothelial function.
        3. AI-driven analytics - Interprets vascular signals and generates meaningful insights.
        
        WHAT JENDO MEASURES:
        - Endothelial function (health of blood vessel lining)
        - Vascular reactivity
        - Early indicators of cardiovascular risk
        
        KEY OUTPUT - VASCULAR HEALTH SCORE:
        Jendo generates a Vascular Health Score, providing a predictive assessment of cardiovascular risk. The score supports early risk identification, enables preventive health monitoring, is NOT a medical diagnosis, and must be interpreted by healthcare professionals.
        
        JENDO HEALTH TEST PROCEDURE:
        - Test Name: Jendo Non-Invasive Vascular Health Test
        - Duration: Approximately 15 minutes
        - Patient Position: Patient lies down comfortably in a supine position
        - Steps: Preparation and positioning, PPG and DTM signal extraction, brief pressure cuff occlusion and release, continued signal monitoring, secure upload of data to cloud for AI analysis
        
        SAFETY & COMFORT:
        - Completely non-invasive
        - No needles
        - No radiation
        - Painless and comfortable
        - Suitable for routine and preventive screening
        
        PATENTED TECHNOLOGY:
        Jendo's core technology is protected by patents in Japan, Sri Lanka, and the USA. These patents cover non-invasive vascular assessment methods, signal processing techniques, and AI-based cardiovascular risk analysis.
        
        WHEN USERS ASK ABOUT HEALTH PROBLEMS:
        - Provide general educational information only
        - Explain that symptoms can have many causes
        - Avoid medical conclusions
        - Encourage consulting a doctor if symptoms persist
        
        TONE & RESPONSE STYLE:
        - Friendly, professional, and reassuring
        - Simple English
        - Short, clear messages
        - Use bullet points when helpful
        - Avoid complex medical jargon
        
        Always end responses about health topics with: "Jendo supports early detection and preventive care but does not replace professional medical advice."
        """;

    @Override
    public ChatResponse sendMessage(ChatRequest request) {
        try {
            if (openRouterApiKey == null || openRouterApiKey.isBlank()) {
                log.error("OpenRouter API key not configured");
                return createErrorResponse("Chat service is not configured. Please contact support.");
            }
            
            String requestBody = buildOpenRouterRequest(request);
            
            log.debug("OpenRouter request body: {}", requestBody);
            
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(OPENROUTER_API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + openRouterApiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            
            log.info("OpenRouter API response status: {}", response.statusCode());
            log.debug("OpenRouter API response body: {}", response.body());
            
            if (response.statusCode() != 200) {
                log.error("OpenRouter API error: {} - {}", response.statusCode(), response.body());
                return createFallbackResponse();
            }
            
            String responseText = extractOpenRouterResponse(response.body());
            
            if (responseText == null || responseText.isBlank()) {
                log.warn("Empty response from OpenRouter API");
                return createFallbackResponse();
            }
            
            return ChatResponse.builder()
                    .id("assistant-" + UUID.randomUUID().toString())
                    .role("assistant")
                    .content(responseText)
                    .timestamp(Instant.now().toString())
                    .build();
                    
        } catch (Exception e) {
            log.error("Error calling OpenRouter API", e);
            return createFallbackResponse();
        }
    }
    
    private ChatResponse createFallbackResponse() {
        String fallbackMessage = """
            I'm sorry, I'm unable to assist with that specific question at the moment.
            
            For more detailed information about Jendo and our cardiovascular health technology, please visit our official website or contact us directly:
            
            **Website:** https://www.jendo.health/
            
            **Jendo Incorporation (USA)**
            - Address: 251, Little Falls Drive, Wilmington, New Castle County, Delaware
            - Email: info@jendoinnovations.com
            - Phone: 0766210120
            
            **AI Health R&D Centre**
            - Address: Bay X, Trace Expert City
            - Email: info@jendoinnovations.com
            - Phone: 0766210120
            
            Our team will be happy to help you with any questions about Jendo technology, the Jendo Health Test, or cardiovascular health monitoring.
            """;
        
        return ChatResponse.builder()
                .id("assistant-" + UUID.randomUUID().toString())
                .role("assistant")
                .content(fallbackMessage)
                .timestamp(Instant.now().toString())
                .build();
    }
    
    private String buildOpenRouterRequest(ChatRequest request) throws Exception {
        ObjectNode root = objectMapper.createObjectNode();
        
        root.put("model", MODEL);
        
        ArrayNode messages = objectMapper.createArrayNode();
        
        // Add system message
        ObjectNode systemMessage = objectMapper.createObjectNode();
        systemMessage.put("role", "system");
        systemMessage.put("content", SYSTEM_PROMPT);
        messages.add(systemMessage);
        
        // Add conversation history
        if (request.getHistory() != null) {
            for (ChatRequest.ChatHistoryItem item : request.getHistory()) {
                ObjectNode message = objectMapper.createObjectNode();
                message.put("role", item.getRole());
                message.put("content", item.getContent());
                messages.add(message);
            }
        }
        
        // Add current user message
        ObjectNode userMessage = objectMapper.createObjectNode();
        userMessage.put("role", "user");
        userMessage.put("content", request.getMessage());
        messages.add(userMessage);
        
        root.set("messages", messages);
        root.put("temperature", 0.7);
        root.put("max_tokens", 1024);
        
        return objectMapper.writeValueAsString(root);
    }
    
    private String extractOpenRouterResponse(String responseBody) throws Exception {
        JsonNode root = objectMapper.readTree(responseBody);
        
        JsonNode error = root.get("error");
        if (error != null) {
            String errorMessage = error.has("message") ? error.get("message").asText() : "Unknown error";
            log.error("OpenRouter API returned error: {}", errorMessage);
            return null;
        }
        
        JsonNode choices = root.get("choices");
        if (choices != null && choices.isArray() && choices.size() > 0) {
            JsonNode firstChoice = choices.get(0);
            JsonNode message = firstChoice.get("message");
            
            if (message != null) {
                JsonNode content = message.get("content");
                if (content != null) {
                    String responseText = content.asText();
                    if (responseText != null && !responseText.isBlank()) {
                        return responseText;
                    }
                }
            }
        }
        
        log.warn("Could not extract response text from OpenRouter response: {}", responseBody);
        return null;
    }
    
    private ChatResponse createErrorResponse(String message) {
        return ChatResponse.builder()
                .id("error-" + UUID.randomUUID().toString())
                .role("assistant")
                .content(message)
                .timestamp(Instant.now().toString())
                .build();
    }
}
