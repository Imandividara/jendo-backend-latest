package com.jendo.app.controller;

import com.jendo.app.common.dto.ApiResponse;
import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.domain.learningmaterial.dto.LearningMaterialRequestDto;
import com.jendo.app.domain.learningmaterial.dto.LearningMaterialResponseDto;
import com.jendo.app.domain.learningmaterial.service.LearningMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/learning-materials")
@RequiredArgsConstructor
@Tag(name = "Learning Materials", description = "Educational content management APIs")
public class LearningMaterialController {

    private final LearningMaterialService learningMaterialService;

    @PostMapping
    @Operation(summary = "Create learning material", description = "Adds new educational content")
    public ResponseEntity<ApiResponse<LearningMaterialResponseDto>> createLearningMaterial(
            @Valid @RequestBody LearningMaterialRequestDto request) {
        LearningMaterialResponseDto material = learningMaterialService.createLearningMaterial(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(material, "Learning material created"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get learning material by ID")
    public ResponseEntity<ApiResponse<LearningMaterialResponseDto>> getLearningMaterialById(@PathVariable Long id) {
        LearningMaterialResponseDto material = learningMaterialService.getLearningMaterialById(id);
        return ResponseEntity.ok(ApiResponse.success(material));
    }

    @GetMapping
    @Operation(summary = "Get all learning materials", description = "Retrieves all materials with pagination")
    public ResponseEntity<ApiResponse<PaginationResponse<LearningMaterialResponseDto>>> getAllLearningMaterials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<LearningMaterialResponseDto> materials = 
                learningMaterialService.getAllLearningMaterials(page, size);
        return ResponseEntity.ok(ApiResponse.success(materials));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get materials by category")
    public ResponseEntity<ApiResponse<PaginationResponse<LearningMaterialResponseDto>>> getMaterialsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<LearningMaterialResponseDto> materials = 
                learningMaterialService.getLearningMaterialsByCategory(category, page, size);
        return ResponseEntity.ok(ApiResponse.success(materials));
    }

    @GetMapping("/search")
    @Operation(summary = "Search materials", description = "Search learning materials by title")
    public ResponseEntity<ApiResponse<PaginationResponse<LearningMaterialResponseDto>>> searchMaterials(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<LearningMaterialResponseDto> materials = 
                learningMaterialService.searchLearningMaterials(query, page, size);
        return ResponseEntity.ok(ApiResponse.success(materials));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update learning material")
    public ResponseEntity<ApiResponse<LearningMaterialResponseDto>> updateLearningMaterial(
            @PathVariable Long id,
            @Valid @RequestBody LearningMaterialRequestDto request) {
        LearningMaterialResponseDto material = learningMaterialService.updateLearningMaterial(id, request);
        return ResponseEntity.ok(ApiResponse.success(material, "Learning material updated"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete learning material")
    public ResponseEntity<Void> deleteLearningMaterial(@PathVariable Long id) {
        learningMaterialService.deleteLearningMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
