package com.jendo.app.controller;

import com.jendo.app.common.dto.ApiResponse;
import com.jendo.app.domain.user.dto.UserResponseDto;
import com.jendo.app.domain.user.entity.User;
import com.jendo.app.domain.user.mapper.UserMapper;
import com.jendo.app.domain.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Home", description = "Home page data APIs")
public class HomeController {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "Get home data", description = "Returns home page data for authenticated user including profile completion status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getHomeData(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Authentication required"));
        }
        
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserResponseDto userDto = userMapper.toResponseDto(user);
        boolean profileComplete = isProfileComplete(user);
        
        Map<String, Object> homeData = new HashMap<>();
        homeData.put("user", userDto);
        homeData.put("profileComplete", profileComplete);
        homeData.put("greeting", getGreeting(user.getFirstName()));
        homeData.put("quickStats", getQuickStats(user.getId()));
        
        log.info("Home data retrieved for user: {}", email);
        return ResponseEntity.ok(ApiResponse.success(homeData, "Home data retrieved successfully"));
    }

    @GetMapping("/profile")
    @Transactional(readOnly = true)
    @Operation(summary = "Get user profile", description = "Returns authenticated user's profile with completion status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProfile(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Authentication required"));
        }
        
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserResponseDto userDto = userMapper.toResponseDto(user);
        boolean profileComplete = isProfileComplete(user);
        
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("user", userDto);
        profileData.put("profileComplete", profileComplete);
        profileData.put("missingFields", getMissingFields(user));
        
        return ResponseEntity.ok(ApiResponse.success(profileData, "Profile retrieved successfully"));
    }

    private boolean isProfileComplete(User user) {
        return user.getFirstName() != null && !user.getFirstName().isEmpty()
            && user.getLastName() != null && !user.getLastName().isEmpty()
            && user.getEmail() != null && !user.getEmail().isEmpty()
            && user.getPhone() != null && !user.getPhone().isEmpty()
            && user.getDateOfBirth() != null
            && user.getGender() != null && !user.getGender().isEmpty();
    }

    private java.util.List<String> getMissingFields(User user) {
        java.util.List<String> missing = new java.util.ArrayList<>();
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) missing.add("firstName");
        if (user.getLastName() == null || user.getLastName().isEmpty()) missing.add("lastName");
        if (user.getPhone() == null || user.getPhone().isEmpty()) missing.add("phone");
        if (user.getDateOfBirth() == null) missing.add("dateOfBirth");
        if (user.getGender() == null || user.getGender().isEmpty()) missing.add("gender");
        return missing;
    }

    private String getGreeting(String firstName) {
        int hour = java.time.LocalTime.now().getHour();
        String timeGreeting;
        if (hour < 12) {
            timeGreeting = "Good morning";
        } else if (hour < 17) {
            timeGreeting = "Good afternoon";
        } else {
            timeGreeting = "Good evening";
        }
        return timeGreeting + ", " + (firstName != null ? firstName : "User") + "!";
    }

    private Map<String, Object> getQuickStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTests", 0);
        stats.put("upcomingAppointments", 0);
        stats.put("unreadNotifications", 0);
        return stats;
    }
}
