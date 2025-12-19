package com.jendo.app.domain.doctor.dto;

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
@Schema(description = "Available time slot for doctor")
public class AvailableSlotDto {

    @Schema(description = "Slot ID", example = "1")
    private Long id;

    @Schema(description = "Doctor ID", example = "1")
    private Long doctorId;

    @Schema(description = "Slot date", example = "2024-12-20")
    private LocalDate slotDate;

    @Schema(description = "Start time", example = "09:00:00")
    private LocalTime startTime;

    @Schema(description = "End time", example = "09:30:00")
    private LocalTime endTime;

    @Schema(description = "Whether slot is booked", example = "false")
    private Boolean isBooked;

    @Schema(description = "Duration in minutes", example = "30")
    private Integer slotDurationMinutes;
}
