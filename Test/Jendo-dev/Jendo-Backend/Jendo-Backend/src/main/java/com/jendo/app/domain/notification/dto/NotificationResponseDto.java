package com.jendo.app.domain.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Notification response data")
public class NotificationResponseDto {

    @Schema(description = "Notification unique identifier", example = "1")
    private Long id;

    @Schema(description = "User ID", example = "1")
    private Long userId;

    @Schema(description = "Notification message", example = "Your appointment is scheduled for tomorrow")
    private String message;

    @Schema(description = "Notification type", example = "APPOINTMENT")
    private String type;

    @Schema(description = "Read status", example = "false")
    private Boolean isRead;

    @Schema(description = "Creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
}
