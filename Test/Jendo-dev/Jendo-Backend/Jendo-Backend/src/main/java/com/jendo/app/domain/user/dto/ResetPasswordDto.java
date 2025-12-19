package com.jendo.app.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Reset password request")
public class ResetPasswordDto {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Valid email is required")
    @Schema(description = "User's email address")
    private String email;
    
    @NotBlank(message = "OTP is required")
    @Schema(description = "OTP code sent to email")
    private String otp;
    
    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Schema(description = "New password")
    private String newPassword;
}
