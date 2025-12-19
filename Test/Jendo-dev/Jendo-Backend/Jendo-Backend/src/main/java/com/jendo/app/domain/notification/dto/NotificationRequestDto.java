package com.jendo.app.domain.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Notification creation request")
public class NotificationRequestDto {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    @Schema(description = "ID of the user to notify", example = "1", required = true)
    private Long userId;

    @NotBlank(message = "Message is required")
    @Schema(description = "Notification message", example = "Your appointment is scheduled for tomorrow", required = true)
    private String message;

    @Schema(description = "Notification type", example = "APPOINTMENT")
    private String type;
}
