package com.jendo.app.controller;

import com.jendo.app.common.dto.ApiResponse;
import com.jendo.app.domain.reportcategory.dto.ReportCategoryRequestDto;
import com.jendo.app.domain.reportcategory.dto.ReportCategoryResponseDto;
import com.jendo.app.domain.reportcategory.service.ReportCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report-categories")
@RequiredArgsConstructor
@Tag(name = "Report Categories", description = "CRUD operations for report categories")
public class ReportCategoryController {

    private final ReportCategoryService service;

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieves all report categories")
    public ResponseEntity<ApiResponse<List<ReportCategoryResponseDto>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.success(service.getAllCategories()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieves a category by its ID")
    public ResponseEntity<ApiResponse<ReportCategoryResponseDto>> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(service.getCategoryById(id)));
    }

    @GetMapping("/{id}/sections")
    @Operation(summary = "Get category with sections", description = "Retrieves a category with all its sections")
    public ResponseEntity<ApiResponse<ReportCategoryResponseDto>> getCategoryWithSections(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(service.getCategoryWithSections(id)));
    }

    @PostMapping
    @Operation(summary = "Create category", description = "Creates a new report category")
    public ResponseEntity<ApiResponse<ReportCategoryResponseDto>> createCategory(
            @Valid @RequestBody ReportCategoryRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(service.createCategory(request), "Category created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Updates an existing report category")
    public ResponseEntity<ApiResponse<ReportCategoryResponseDto>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody ReportCategoryRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(service.updateCategory(id, request), "Category updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Deletes a report category")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        service.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Category deleted successfully"));
    }
}
