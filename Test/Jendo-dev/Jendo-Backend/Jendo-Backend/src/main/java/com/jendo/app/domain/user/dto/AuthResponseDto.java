package com.jendo.app.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication response data")
public class AuthResponseDto {
    
    @Schema(description = "JWT access token")
    private String token;
    
    @Schema(description = "JWT refresh token")
    private String refreshToken;
    
    @Schema(description = "User ID")
    private Long userId;
    
    @Schema(description = "User email")
    private String email;
    
    @Schema(description = "User's full name")
    private String fullName;
    
    @Schema(description = "User profile data")
    private UserResponseDto user;
    
    @Schema(description = "Indicates if user profile is complete")
    private Boolean profileComplete;
}
