package com.jendo.app.domain.notification.mapper;

import com.jendo.app.domain.notification.dto.NotificationRequestDto;
import com.jendo.app.domain.notification.dto.NotificationResponseDto;
import com.jendo.app.domain.notification.entity.Notification;
import com.jendo.app.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public Notification toEntity(NotificationRequestDto dto, User user) {
        return Notification.builder()
                .user(user)
                .message(dto.getMessage())
                .type(dto.getType())
                .isRead(false)
                .build();
    }

    public NotificationResponseDto toResponseDto(Notification entity) {
        return NotificationResponseDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .message(entity.getMessage())
                .type(entity.getType())
                .isRead(entity.getIsRead())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
