package com.jendo.app.controller;

import com.jendo.app.common.dto.ApiResponse;
import com.jendo.app.domain.reportitemvalue.dto.ReportItemValueRequestDto;
import com.jendo.app.domain.reportitemvalue.dto.ReportItemValueResponseDto;
import com.jendo.app.domain.reportitemvalue.service.ReportItemValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/report-values")
@RequiredArgsConstructor
@Tag(name = "Report Item Values", description = "CRUD operations for report values with attachment support")
public class ReportItemValueController {

    private final ReportItemValueService service;
    private final com.jendo.app.domain.user.service.UserService userService;

    @GetMapping
    @Operation(summary = "Get all values", description = "Retrieves all report values for the authenticated user")
    public ResponseEntity<ApiResponse<List<ReportItemValueResponseDto>>> getAllValues(Authentication authentication) {
        String email = authentication.getName();
        Long userId = userService.getUserByEmail(email).getId();
        return ResponseEntity.ok(ApiResponse.success(service.getValuesByUserId(userId)));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get values by user", description = "Retrieves all values for a user")
    public ResponseEntity<ApiResponse<List<ReportItemValueResponseDto>>> getValuesByUser(
            @PathVariable Long userId,
            Authentication authentication) {
        // Ensure users can only access their own data
        String email = authentication.getName();
        Long authenticatedUserId = userService.getUserByEmail(email).getId();
        if (!userId.equals(authenticatedUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Access denied: You can only access your own reports"));
        }
        return ResponseEntity.ok(ApiResponse.success(service.getValuesByUserId(userId)));
    }

    @GetMapping("/item/{reportItemId}")
    @Operation(summary = "Get values by report item", description = "Retrieves all values for a report item for the authenticated user")
    public ResponseEntity<ApiResponse<List<ReportItemValueResponseDto>>> getValuesByReportItem(
            @PathVariable Long reportItemId,
            Authentication authentication) {
        String email = authentication.getName();
        Long userId = userService.getUserByEmail(email).getId();
        return ResponseEntity.ok(ApiResponse.success(service.getValuesByUserIdAndReportItemId(userId, reportItemId)));
    }

    @GetMapping("/user/{userId}/item/{reportItemId}")
    @Operation(summary = "Get values by user and item", description = "Retrieves all values for a user and report item")
    public ResponseEntity<ApiResponse<List<ReportItemValueResponseDto>>> getValuesByUserAndItem(
            @PathVariable Long userId,
            @PathVariable Long reportItemId,
            Authentication authentication) {
        // Ensure users can only access their own data
        String email = authentication.getName();
        Long authenticatedUserId = userService.getUserByEmail(email).getId();
        if (!userId.equals(authenticatedUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Access denied: You can only access your own reports"));
        }
        return ResponseEntity.ok(ApiResponse.success(service.getValuesByUserIdAndReportItemId(userId, reportItemId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get value by ID", description = "Retrieves a value by its ID")
    public ResponseEntity<ApiResponse<ReportItemValueResponseDto>> getValueById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(service.getValueById(id)));
    }

    @PostMapping
    @Operation(summary = "Create value", description = "Creates a new report item value")
    public ResponseEntity<ApiResponse<ReportItemValueResponseDto>> createValue(
            @Valid @RequestBody ReportItemValueRequestDto request,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(service.createValue(request, email), "Value created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update value", description = "Updates an existing report item value")
    public ResponseEntity<ApiResponse<ReportItemValueResponseDto>> updateValue(
            @PathVariable Long id,
            @Valid @RequestBody ReportItemValueRequestDto request,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(ApiResponse.success(service.updateValue(id, request, email), "Value updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete value", description = "Deletes a report item value and its attachments")
    public ResponseEntity<ApiResponse<Void>> deleteValue(@PathVariable Long id) {
        service.deleteValue(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Value deleted successfully"));
    }

    @PostMapping(value = "/{id}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Add attachment", description = "Adds an attachment to a report item value")
    public ResponseEntity<ApiResponse<ReportItemValueResponseDto>> addAttachment(
            @PathVariable Long id,
            @Parameter(description = "File to upload", required = true,
                content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(type = "string", format = "binary")))
            @RequestPart("file") MultipartFile file,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(service.addAttachment(id, file, email), "Attachment added successfully"));
    }

    @DeleteMapping("/attachments/{attachmentId}")
    @Operation(summary = "Delete attachment", description = "Deletes an attachment")
    public ResponseEntity<ApiResponse<Void>> deleteAttachment(@PathVariable Long attachmentId) {
        service.deleteAttachment(attachmentId);
        return ResponseEntity.ok(ApiResponse.success(null, "Attachment deleted successfully"));
    }

    @GetMapping("/attachments/{attachmentId}/download")
    @Operation(summary = "Download attachment", description = "Downloads an attachment file")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long attachmentId) {
        Resource resource = service.downloadAttachment(attachmentId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
