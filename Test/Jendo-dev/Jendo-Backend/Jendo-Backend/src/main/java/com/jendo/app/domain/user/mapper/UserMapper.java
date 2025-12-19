package com.jendo.app.domain.user.mapper;

import com.jendo.app.domain.user.dto.UserRequestDto;
import com.jendo.app.domain.user.dto.UserResponseDto;
import com.jendo.app.domain.user.entity.Role;
import com.jendo.app.domain.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(UserRequestDto dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .dateOfBirth(dto.getDateOfBirth())
                .gender(dto.getGender())
                .profileImage(dto.getProfileImage())
                .nationality(dto.getNationality())
                .address(dto.getAddress())
                .build();
    }

    public UserResponseDto toResponseDto(User entity) {
        return UserResponseDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .dateOfBirth(entity.getDateOfBirth())
                .gender(entity.getGender())
                .profileImage(entity.getProfileImage())
                .nationality(entity.getNationality())
                .address(entity.getAddress())
                .weight(entity.getWeight())
                .height(entity.getHeight())
                .roles(entity.getRoles() != null 
                        ? entity.getRoles().stream()
                                .map(Role::getRoleName)
                                .collect(Collectors.toList())
                        : Collections.emptyList())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
