package com.jendo.app.controller;

import com.jendo.app.common.dto.ApiResponse;
import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.domain.healthparameter.dto.HealthParameterRequestDto;
import com.jendo.app.domain.healthparameter.dto.HealthParameterResponseDto;
import com.jendo.app.domain.healthparameter.service.HealthParameterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health-parameters")
@RequiredArgsConstructor
@Tag(name = "Health Parameters", description = "Health parameter management APIs")
public class HealthParameterController {

    private final HealthParameterService healthParameterService;

    @PostMapping
    @Operation(summary = "Create health parameter", description = "Records new health parameters")
    public ResponseEntity<ApiResponse<HealthParameterResponseDto>> createHealthParameter(
            @Valid @RequestBody HealthParameterRequestDto request) {
        HealthParameterResponseDto param = healthParameterService.createHealthParameter(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(param, "Health parameter created"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get health parameter by ID")
    public ResponseEntity<ApiResponse<HealthParameterResponseDto>> getHealthParameterById(@PathVariable Long id) {
        HealthParameterResponseDto param = healthParameterService.getHealthParameterById(id);
        return ResponseEntity.ok(ApiResponse.success(param));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get health parameters by user")
    public ResponseEntity<ApiResponse<PaginationResponse<HealthParameterResponseDto>>> getHealthParametersByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<HealthParameterResponseDto> params = 
                healthParameterService.getHealthParametersByUserId(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(params));
    }

    @GetMapping("/user/{userId}/latest")
    @Operation(summary = "Get latest health parameter", description = "Gets the most recent health parameters for a user")
    public ResponseEntity<ApiResponse<HealthParameterResponseDto>> getLatestHealthParameter(@PathVariable Long userId) {
        HealthParameterResponseDto param = healthParameterService.getLatestHealthParameterByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(param));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update health parameter")
    public ResponseEntity<ApiResponse<HealthParameterResponseDto>> updateHealthParameter(
            @PathVariable Long id,
            @Valid @RequestBody HealthParameterRequestDto request) {
        HealthParameterResponseDto param = healthParameterService.updateHealthParameter(id, request);
        return ResponseEntity.ok(ApiResponse.success(param, "Health parameter updated"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete health parameter")
    public ResponseEntity<Void> deleteHealthParameter(@PathVariable Long id) {
        healthParameterService.deleteHealthParameter(id);
        return ResponseEntity.noContent().build();
    }
}
