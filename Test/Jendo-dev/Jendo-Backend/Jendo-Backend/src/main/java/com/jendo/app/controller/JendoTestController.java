package com.jendo.app.controller;

import com.jendo.app.common.dto.ApiResponse;
import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.domain.jendotest.dto.JendoTestRequestDto;
import com.jendo.app.domain.jendotest.dto.JendoTestResponseDto;
import com.jendo.app.domain.jendotest.service.JendoTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/jendo-tests")
@RequiredArgsConstructor
@Tag(name = "Jendo Tests", description = "Jendo health test management APIs")
public class JendoTestController {

    private final JendoTestService jendoTestService;

    @PostMapping
    @Operation(summary = "Create a new Jendo test", description = "Records a new Jendo health test result")
    public ResponseEntity<ApiResponse<JendoTestResponseDto>> createTest(@Valid @RequestBody JendoTestRequestDto request) {
        JendoTestResponseDto test = jendoTestService.createTest(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(test, "Jendo test created successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get test by ID", description = "Retrieves a Jendo test by its ID")
    public ResponseEntity<ApiResponse<JendoTestResponseDto>> getTestById(@PathVariable Long id) {
        JendoTestResponseDto test = jendoTestService.getTestById(id);
        return ResponseEntity.ok(ApiResponse.success(test));
    }

    @GetMapping
    @Operation(summary = "Get all tests", description = "Retrieves all Jendo tests with pagination")
    public ResponseEntity<ApiResponse<PaginationResponse<JendoTestResponseDto>>> getAllTests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<JendoTestResponseDto> tests = jendoTestService.getAllTests(page, size);
        return ResponseEntity.ok(ApiResponse.success(tests));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get tests by user", description = "Retrieves all tests for a specific user")
    public ResponseEntity<ApiResponse<PaginationResponse<JendoTestResponseDto>>> getTestsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<JendoTestResponseDto> tests = jendoTestService.getTestsByUserId(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(tests));
    }

    @GetMapping("/user/{userId}/date-range")
    @Operation(summary = "Get tests by date range", description = "Retrieves tests for a user within a date range")
    public ResponseEntity<ApiResponse<List<JendoTestResponseDto>>> getTestsByDateRange(
            @PathVariable Long userId,
            @Parameter(description = "Start date (yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<JendoTestResponseDto> tests = jendoTestService.getTestsByUserIdAndDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(tests));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update test", description = "Updates an existing Jendo test")
    public ResponseEntity<ApiResponse<JendoTestResponseDto>> updateTest(
            @PathVariable Long id,
            @Valid @RequestBody JendoTestRequestDto request) {
        JendoTestResponseDto test = jendoTestService.updateTest(id, request);
        return ResponseEntity.ok(ApiResponse.success(test, "Jendo test updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete test", description = "Deletes a Jendo test by ID")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        jendoTestService.deleteTest(id);
        return ResponseEntity.noContent().build();
    }
}
