package com.jendo.app.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User creation/update request")
public class UserRequestDto {

    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
    @Schema(description = "User's first name", example = "John", required = true)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    @Schema(description = "User's last name", example = "Doe", required = true)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(description = "User's email address", example = "john.doe@example.com", required = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    @Schema(description = "User's password", example = "securePassword123", required = true)
    private String password;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Schema(description = "User's phone number", example = "+1234567890")
    private String phone;

    @Schema(description = "User's date of birth", example = "1990-01-15")
    private LocalDate dateOfBirth;

    @Size(max = 20, message = "Gender must not exceed 20 characters")
    @Schema(description = "User's gender", example = "Male")
    private String gender;

    @Schema(description = "URL to user's profile image", example = "https://example.com/profile.jpg")
    private String profileImage;

    @Size(max = 100, message = "Nationality must not exceed 100 characters")
    @Schema(description = "User's nationality", example = "American")
    private String nationality;

    @Schema(description = "User's address", example = "123 Main St, City, Country")
    private String address;

    @Schema(description = "List of role names for the user", example = "[\"USER\", \"PATIENT\"]")
    private List<String> roles;
}
