package com.jendo.app.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User response data")
public class UserResponseDto {

    @Schema(description = "User's unique identifier", example = "1")
    private Long id;

    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "User's phone number", example = "+1234567890")
    private String phone;

    @Schema(description = "User's date of birth", example = "1990-01-15")
    private LocalDate dateOfBirth;

    @Schema(description = "User's gender", example = "Male")
    private String gender;

    @Schema(description = "URL to user's profile image", example = "https://example.com/profile.jpg")
    private String profileImage;

    @Schema(description = "User's nationality", example = "American")
    private String nationality;

    @Schema(description = "User's address", example = "123 Main St, City, Country")
    private String address;

    @Schema(description = "User's weight in kg", example = "70.5")
    private Double weight;

    @Schema(description = "User's height in cm", example = "175.0")
    private Double height;

    @Schema(description = "List of user's roles", example = "[\"USER\", \"PATIENT\"]")
    private List<String> roles;

    @Schema(description = "Account creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;
}
