package com.jendo.app.controller;

import com.jendo.app.common.dto.ApiResponse;
import com.jendo.app.domain.reportsection.dto.ReportSectionRequestDto;
import com.jendo.app.domain.reportsection.dto.ReportSectionResponseDto;
import com.jendo.app.domain.reportsection.service.ReportSectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report-sections")
@RequiredArgsConstructor
@Tag(name = "Report Sections", description = "CRUD operations for report sections")
public class ReportSectionController {

    private final ReportSectionService service;

    @GetMapping
    @Operation(summary = "Get all sections", description = "Retrieves all report sections")
    public ResponseEntity<ApiResponse<List<ReportSectionResponseDto>>> getAllSections() {
        return ResponseEntity.ok(ApiResponse.success(service.getAllSections()));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get sections by category", description = "Retrieves all sections for a category")
    public ResponseEntity<ApiResponse<List<ReportSectionResponseDto>>> getSectionsByCategory(
            @PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(service.getSectionsByCategoryId(categoryId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get section by ID", description = "Retrieves a section by its ID")
    public ResponseEntity<ApiResponse<ReportSectionResponseDto>> getSectionById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(service.getSectionById(id)));
    }

    @GetMapping("/{id}/items")
    @Operation(summary = "Get section with items", description = "Retrieves a section with all its items")
    public ResponseEntity<ApiResponse<ReportSectionResponseDto>> getSectionWithItems(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(service.getSectionWithItems(id)));
    }

    @PostMapping
    @Operation(summary = "Create section", description = "Creates a new report section")
    public ResponseEntity<ApiResponse<ReportSectionResponseDto>> createSection(
            @Valid @RequestBody ReportSectionRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(service.createSection(request), "Section created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update section", description = "Updates an existing report section")
    public ResponseEntity<ApiResponse<ReportSectionResponseDto>> updateSection(
            @PathVariable Long id,
            @Valid @RequestBody ReportSectionRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(service.updateSection(id, request), "Section updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete section", description = "Deletes a report section")
    public ResponseEntity<ApiResponse<Void>> deleteSection(@PathVariable Long id) {
        service.deleteSection(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Section deleted successfully"));
    }
}
