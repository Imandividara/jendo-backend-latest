package com.jendo.app.domain.doctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Doctor response data")
public class DoctorResponseDto {

    @Schema(description = "Doctor's unique identifier", example = "1")
    private Long id;

    @Schema(description = "Doctor's full name", example = "Dr. Jane Smith")
    private String name;

    @Schema(description = "Doctor's specialty", example = "Cardiology")
    private String specialty;

    @Schema(description = "Hospital name", example = "City General Hospital")
    private String hospital;

    @Schema(description = "Doctor's email", example = "jane.smith@hospital.com")
    private String email;

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

    @Schema(description = "Consultation fees")
    private List<ConsultationFeeDto> consultationFees;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Consultation fee details")
    public static class ConsultationFeeDto {
        @Schema(description = "Fee ID", example = "1")
        private Long id;
        @Schema(description = "Fee type", example = "Initial Consultation")
        private String feeType;
        @Schema(description = "Fee amount", example = "150.00")
        private java.math.BigDecimal amount;
        @Schema(description = "Currency", example = "USD")
        private String currency;
    }
}
