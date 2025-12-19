package com.jendo.app.controller;

import com.jendo.app.common.dto.ApiResponse;
import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.domain.notification.dto.NotificationRequestDto;
import com.jendo.app.domain.notification.dto.NotificationResponseDto;
import com.jendo.app.domain.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Notification management APIs")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Create notification", description = "Creates a new notification")
    public ResponseEntity<ApiResponse<NotificationResponseDto>> createNotification(
            @Valid @RequestBody NotificationRequestDto request) {
        NotificationResponseDto notification = notificationService.createNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(notification, "Notification created"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID")
    public ResponseEntity<ApiResponse<NotificationResponseDto>> getNotificationById(@PathVariable Long id) {
        NotificationResponseDto notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(ApiResponse.success(notification));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get notifications by user", description = "Retrieves notifications for a user")
    public ResponseEntity<ApiResponse<PaginationResponse<NotificationResponseDto>>> getNotificationsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<NotificationResponseDto> notifications = 
                notificationService.getNotificationsByUserId(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(notifications));
    }

    @GetMapping("/user/{userId}/unread")
    @Operation(summary = "Get unread notifications", description = "Retrieves unread notifications for a user")
    public ResponseEntity<ApiResponse<List<NotificationResponseDto>>> getUnreadNotifications(@PathVariable Long userId) {
        List<NotificationResponseDto> notifications = notificationService.getUnreadNotificationsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(notifications));
    }

    @GetMapping("/user/{userId}/unread/count")
    @Operation(summary = "Get unread count", description = "Gets the count of unread notifications")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(@PathVariable Long userId) {
        long count = notificationService.getUnreadCountByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark as read", description = "Marks a notification as read")
    public ResponseEntity<ApiResponse<NotificationResponseDto>> markAsRead(@PathVariable Long id) {
        NotificationResponseDto notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success(notification, "Notification marked as read"));
    }

    @PatchMapping("/user/{userId}/read-all")
    @Operation(summary = "Mark all as read", description = "Marks all notifications as read for a user")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(ApiResponse.success(null, "All notifications marked as read"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete notification")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
