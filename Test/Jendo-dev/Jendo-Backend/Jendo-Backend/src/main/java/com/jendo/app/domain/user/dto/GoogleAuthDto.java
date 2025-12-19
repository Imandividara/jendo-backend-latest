package com.jendo.app.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Google OAuth authentication request")
public class GoogleAuthDto {
    
    @NotBlank(message = "Google ID token is required")
    @Schema(description = "Google OAuth ID token from client")
    private String idToken;
    
    @Schema(description = "Google access token (optional)")
    private String accessToken;
}
