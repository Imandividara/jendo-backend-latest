package com.jendo.app.controller;

import com.jendo.app.common.dto.ApiResponse;
import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.domain.appointment.dto.AppointmentRequestDto;
import com.jendo.app.domain.appointment.dto.AppointmentResponseDto;
import com.jendo.app.domain.appointment.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Appointment management APIs")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "Create appointment", description = "Schedules a new appointment")
    public ResponseEntity<ApiResponse<AppointmentResponseDto>> createAppointment(
            @Valid @RequestBody AppointmentRequestDto request) {
        AppointmentResponseDto appointment = appointmentService.createAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(appointment, "Appointment created successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID", description = "Retrieves an appointment by its ID")
    public ResponseEntity<ApiResponse<AppointmentResponseDto>> getAppointmentById(@PathVariable Long id) {
        AppointmentResponseDto appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(ApiResponse.success(appointment));
    }

    @GetMapping
    @Operation(summary = "Get all appointments", description = "Retrieves all appointments with pagination")
    public ResponseEntity<ApiResponse<PaginationResponse<AppointmentResponseDto>>> getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<AppointmentResponseDto> appointments = appointmentService.getAllAppointments(page, size);
        return ResponseEntity.ok(ApiResponse.success(appointments));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get appointments by user", description = "Retrieves appointments for a specific user")
    public ResponseEntity<ApiResponse<PaginationResponse<AppointmentResponseDto>>> getAppointmentsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<AppointmentResponseDto> appointments = 
                appointmentService.getAppointmentsByUserId(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(appointments));
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get appointments by doctor", description = "Retrieves appointments for a specific doctor")
    public ResponseEntity<ApiResponse<PaginationResponse<AppointmentResponseDto>>> getAppointmentsByDoctorId(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<AppointmentResponseDto> appointments = 
                appointmentService.getAppointmentsByDoctorId(doctorId, page, size);
        return ResponseEntity.ok(ApiResponse.success(appointments));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update appointment", description = "Updates an existing appointment")
    public ResponseEntity<ApiResponse<AppointmentResponseDto>> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequestDto request) {
        AppointmentResponseDto appointment = appointmentService.updateAppointment(id, request);
        return ResponseEntity.ok(ApiResponse.success(appointment, "Appointment updated successfully"));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update appointment status", description = "Updates the status of an appointment")
    public ResponseEntity<ApiResponse<AppointmentResponseDto>> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        AppointmentResponseDto appointment = appointmentService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(appointment, "Appointment status updated"));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel appointment", description = "Cancels an appointment by setting status to CANCELLED")
    public ResponseEntity<ApiResponse<AppointmentResponseDto>> cancelAppointment(@PathVariable Long id) {
        AppointmentResponseDto appointment = appointmentService.updateAppointmentStatus(id, "CANCELLED");
        return ResponseEntity.ok(ApiResponse.success(appointment, "Appointment cancelled successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete appointment", description = "Permanently deletes an appointment")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
