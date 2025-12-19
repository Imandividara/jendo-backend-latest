package com.jendo.app.controller;

import com.jendo.app.common.dto.ApiResponse;
import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.domain.doctor.dto.AvailableSlotDto;
import com.jendo.app.domain.doctor.dto.DoctorRequestDto;
import com.jendo.app.domain.doctor.dto.DoctorResponseDto;
import com.jendo.app.domain.doctor.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctors", description = "Doctor management APIs")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @Operation(summary = "Create a new doctor", description = "Adds a new doctor to the system")
    public ResponseEntity<ApiResponse<DoctorResponseDto>> createDoctor(@Valid @RequestBody DoctorRequestDto request) {
        DoctorResponseDto doctor = doctorService.createDoctor(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(doctor, "Doctor created successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get doctor by ID", description = "Retrieves a doctor by their ID")
    public ResponseEntity<ApiResponse<DoctorResponseDto>> getDoctorById(@PathVariable Long id) {
        DoctorResponseDto doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(ApiResponse.success(doctor));
    }

    @GetMapping
    @Operation(summary = "Get all doctors", description = "Retrieves all doctors with pagination")
    public ResponseEntity<ApiResponse<PaginationResponse<DoctorResponseDto>>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<DoctorResponseDto> doctors = doctorService.getAllDoctors(page, size);
        return ResponseEntity.ok(ApiResponse.success(doctors));
    }

    @GetMapping("/specialty/{specialty}")
    @Operation(summary = "Get doctors by specialty", description = "Retrieves doctors filtered by specialty")
    public ResponseEntity<ApiResponse<PaginationResponse<DoctorResponseDto>>> getDoctorsBySpecialty(
            @PathVariable String specialty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<DoctorResponseDto> doctors = doctorService.getDoctorsBySpecialty(specialty, page, size);
        return ResponseEntity.ok(ApiResponse.success(doctors));
    }

    @GetMapping("/available")
    @Operation(summary = "Get available doctors", description = "Retrieves all currently available doctors")
    public ResponseEntity<ApiResponse<List<DoctorResponseDto>>> getAvailableDoctors() {
        List<DoctorResponseDto> doctors = doctorService.getAvailableDoctors();
        return ResponseEntity.ok(ApiResponse.success(doctors));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update doctor", description = "Updates an existing doctor's information")
    public ResponseEntity<ApiResponse<DoctorResponseDto>> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorRequestDto request) {
        DoctorResponseDto doctor = doctorService.updateDoctor(id, request);
        return ResponseEntity.ok(ApiResponse.success(doctor, "Doctor updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete doctor", description = "Removes a doctor from the system")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/available-slots")
    @Operation(summary = "Get available slots", description = "Retrieves available time slots for a doctor on a specific date")
    public ResponseEntity<ApiResponse<List<AvailableSlotDto>>> getAvailableSlots(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AvailableSlotDto> slots = doctorService.getAvailableSlots(id, date);
        return ResponseEntity.ok(ApiResponse.success(slots));
    }

    @GetMapping("/{id}/available-dates")
    @Operation(summary = "Get available dates", description = "Retrieves dates with available slots for a doctor")
    public ResponseEntity<ApiResponse<List<LocalDate>>> getAvailableDates(@PathVariable Long id) {
        List<LocalDate> dates = doctorService.getAvailableDates(id);
        return ResponseEntity.ok(ApiResponse.success(dates));
    }

    @PostMapping("/{id}/available-slots")
    @Operation(summary = "Create available slot", description = "Creates a new available time slot for a doctor (Admin)")
    public ResponseEntity<ApiResponse<AvailableSlotDto>> createAvailableSlot(
            @PathVariable Long id,
            @Valid @RequestBody AvailableSlotDto slotDto) {
        AvailableSlotDto slot = doctorService.createAvailableSlot(id, slotDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(slot, "Available slot created successfully"));
    }
}
