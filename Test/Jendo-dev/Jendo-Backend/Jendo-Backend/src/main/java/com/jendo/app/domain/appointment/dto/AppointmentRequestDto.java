package com.jendo.app.domain.appointment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Appointment creation/update request")
public class AppointmentRequestDto {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    @Schema(description = "ID of the patient", example = "1", required = true)
    private Long userId;

    @Positive(message = "Doctor ID must be positive")
    @Schema(description = "ID of the doctor", example = "1")
    private Long doctorId;

    @Email(message = "Email must be valid")
    @Schema(description = "Patient's email for appointment", example = "patient@example.com")
    private String email;

    @NotNull(message = "Date is required")
    @Schema(description = "Appointment date", example = "2024-01-20", required = true)
    private LocalDate date;

    @NotNull(message = "Time is required")
    @Schema(description = "Appointment time", example = "10:30:00", required = true)
    private LocalTime time;

    @Schema(description = "Specialty for the appointment", example = "Cardiology")
    private String specialty;

    @Schema(description = "Required qualifications", example = "MD")
    private String qualifications;

    @Schema(description = "Doctor's name", example = "Dr. Jane Smith")
    private String doctorName;

    @Schema(description = "Appointment type", example = "IN_PERSON")
    private String type;

    @Schema(description = "Appointment status", example = "SCHEDULED")
    private String status;
}
