package com.jendo.app.controller;

import com.jendo.app.common.dto.ApiResponse;
import com.jendo.app.domain.reportitem.dto.ReportItemRequestDto;
import com.jendo.app.domain.reportitem.dto.ReportItemResponseDto;
import com.jendo.app.domain.reportitem.service.ReportItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report-items")
@RequiredArgsConstructor
@Tag(name = "Report Items", description = "CRUD operations for report items")
public class ReportItemController {

    private final ReportItemService service;

    @GetMapping
    @Operation(summary = "Get all items", description = "Retrieves all report items")
    public ResponseEntity<ApiResponse<List<ReportItemResponseDto>>> getAllItems() {
        return ResponseEntity.ok(ApiResponse.success(service.getAllItems()));
    }

    @GetMapping("/section/{sectionId}")
    @Operation(summary = "Get items by section", description = "Retrieves all items for a section")
    public ResponseEntity<ApiResponse<List<ReportItemResponseDto>>> getItemsBySection(
            @PathVariable Long sectionId) {
        return ResponseEntity.ok(ApiResponse.success(service.getItemsBySectionId(sectionId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get item by ID", description = "Retrieves an item by its ID")
    public ResponseEntity<ApiResponse<ReportItemResponseDto>> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(service.getItemById(id)));
    }

    @PostMapping
    @Operation(summary = "Create item", description = "Creates a new report item")
    public ResponseEntity<ApiResponse<ReportItemResponseDto>> createItem(
            @Valid @RequestBody ReportItemRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(service.createItem(request), "Item created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update item", description = "Updates an existing report item")
    public ResponseEntity<ApiResponse<ReportItemResponseDto>> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody ReportItemRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(service.updateItem(id, request), "Item updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete item", description = "Deletes a report item")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        service.deleteItem(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Item deleted successfully"));
    }
}
