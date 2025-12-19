package com.jendo.app.domain.doctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Doctor creation/update request")
public class DoctorRequestDto {

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 200, message = "Name must be between 1 and 200 characters")
    @Schema(description = "Doctor's full name", example = "Dr. Jane Smith", required = true)
    private String name;

    @Size(max = 100, message = "Specialty must not exceed 100 characters")
    @Schema(description = "Doctor's specialty", example = "Cardiology")
    private String specialty;

    @Size(max = 200, message = "Hospital must not exceed 200 characters")
    @Schema(description = "Hospital name", example = "City General Hospital")
    private String hospital;

    @Email(message = "Email must be valid")
    @Schema(description = "Doctor's email", example = "jane.smith@hospital.com")
    private String email;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Schema(description = "Doctor's phone", example = "+1234567890")
    private String phone;

    @Schema(description = "Doctor's qualifications", example = "MD, PhD, FACC")
    private String qualifications;

    @Schema(description = "URL to doctor's image", example = "https://example.com/doctor.jpg")
    private String imageUrl;

    @Schema(description = "Doctor's office address", example = "456 Medical Center Dr")
    private String address;

    @Schema(description = "Availability status", example = "true")
    private Boolean isAvailable;

    @Schema(description = "Available days", example = "Monday, Wednesday, Friday")
    private String availableDays;
}
