package com.jendo.app.domain.appointment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Appointment response data")
public class AppointmentResponseDto {

    @Schema(description = "Appointment unique identifier", example = "1")
    private Long id;

    @Schema(description = "Patient's user ID", example = "1")
    private Long userId;

    @Schema(description = "Patient's name", example = "John Doe")
    private String userName;

    @Schema(description = "Doctor's ID", example = "1")
    private Long doctorId;

    @Schema(description = "Doctor's name", example = "Dr. Jane Smith")
    private String doctorName;

    @Schema(description = "Patient's email", example = "patient@example.com")
    private String email;

    @Schema(description = "Appointment date", example = "2024-01-20")
    private LocalDate date;

    @Schema(description = "Appointment time", example = "10:30:00")
    private LocalTime time;

    @Schema(description = "Specialty", example = "Cardiology")
    private String specialty;

    @Schema(description = "Required qualifications", example = "MD")
    private String qualifications;

    @Schema(description = "Appointment type", example = "IN_PERSON")
    private String type;

    @Schema(description = "Appointment status", example = "SCHEDULED")
    private String status;
}
