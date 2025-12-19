package com.jendo.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.domain.notification.dto.NotificationRequestDto;
import com.jendo.app.domain.notification.dto.NotificationResponseDto;
import com.jendo.app.domain.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    private NotificationRequestDto notificationRequest;
    private NotificationResponseDto notificationResponse;

    @BeforeEach
    void setUp() {
        notificationRequest = NotificationRequestDto.builder()
                .userId(1L)
                .message("Your appointment is scheduled")
                .type("APPOINTMENT")
                .build();

        notificationResponse = NotificationResponseDto.builder()
                .id(1L)
                .userId(1L)
                .message("Your appointment is scheduled")
                .type("APPOINTMENT")
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Create notification - Success")
    void createNotification_Success() throws Exception {
        when(notificationService.createNotification(any(NotificationRequestDto.class))).thenReturn(notificationResponse);

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notificationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.message").value("Your appointment is scheduled"));

        verify(notificationService, times(1)).createNotification(any(NotificationRequestDto.class));
    }

    @Test
    @DisplayName("Create notification - Validation error for blank message")
    void createNotification_ValidationError_BlankMessage() throws Exception {
        notificationRequest.setMessage("");

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notificationRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get notification by ID - Success")
    void getNotificationById_Success() throws Exception {
        when(notificationService.getNotificationById(1L)).thenReturn(notificationResponse);

        mockMvc.perform(get("/api/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(notificationService, times(1)).getNotificationById(1L);
    }

    @Test
    @DisplayName("Get notifications by user - Success")
    void getNotificationsByUserId_Success() throws Exception {
        List<NotificationResponseDto> notifications = Arrays.asList(notificationResponse);
        PaginationResponse<NotificationResponseDto> paginationResponse = PaginationResponse.<NotificationResponseDto>builder()
                .content(notifications)
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(notificationService.getNotificationsByUserId(eq(1L), eq(0), eq(10))).thenReturn(paginationResponse);

        mockMvc.perform(get("/api/notifications/user/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray());

        verify(notificationService, times(1)).getNotificationsByUserId(eq(1L), eq(0), eq(10));
    }

    @Test
    @DisplayName("Get unread notifications - Success")
    void getUnreadNotifications_Success() throws Exception {
        List<NotificationResponseDto> notifications = Arrays.asList(notificationResponse);
        when(notificationService.getUnreadNotificationsByUserId(1L)).thenReturn(notifications);

        mockMvc.perform(get("/api/notifications/user/1/unread"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());

        verify(notificationService, times(1)).getUnreadNotificationsByUserId(1L);
    }

    @Test
    @DisplayName("Mark as read - Success")
    void markAsRead_Success() throws Exception {
        notificationResponse.setIsRead(true);
        when(notificationService.markAsRead(1L)).thenReturn(notificationResponse);

        mockMvc.perform(patch("/api/notifications/1/read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.isRead").value(true));

        verify(notificationService, times(1)).markAsRead(1L);
    }

    @Test
    @DisplayName("Delete notification - Success")
    void deleteNotification_Success() throws Exception {
        doNothing().when(notificationService).deleteNotification(1L);

        mockMvc.perform(delete("/api/notifications/1"))
                .andExpect(status().isNoContent());

        verify(notificationService, times(1)).deleteNotification(1L);
    }
}
