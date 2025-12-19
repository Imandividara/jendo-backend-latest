package com.jendo.app.controller;

import com.jendo.app.common.dto.ApiResponse;
import com.jendo.app.domain.jendoreport.dto.JendoReportResponseDto;
import com.jendo.app.domain.jendoreport.service.JendoReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/jendo-reports")
@RequiredArgsConstructor
@Tag(name = "Jendo Reports", description = "Jendo PDF report upload and management APIs")
public class JendoReportController {

    private final JendoReportService jendoReportService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Upload Jendo report PDF",
        description = "Uploads a PDF document as a Jendo report for a specific user"
    )
    public ResponseEntity<ApiResponse<JendoReportResponseDto>> uploadReport(
            @Parameter(description = "User ID", required = true)
            @RequestParam Long userId,
            @Parameter(description = "PDF file to upload", required = true,
                content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(type = "string", format = "binary")))
            @RequestPart("file") MultipartFile file,
            @Parameter(description = "Optional description for the report")
            @RequestParam(required = false) String description) {
        
        JendoReportResponseDto report = jendoReportService.uploadReport(userId, file, description);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(report, "Jendo report uploaded successfully"));
    }

    @GetMapping("/user/{userId}")
    @Operation(
        summary = "Get reports by user",
        description = "Retrieves all Jendo reports for a specific user"
    )
    public ResponseEntity<ApiResponse<List<JendoReportResponseDto>>> getReportsByUser(
            @PathVariable Long userId) {
        List<JendoReportResponseDto> reports = jendoReportService.getReportsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(reports));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get report by ID",
        description = "Retrieves a Jendo report by its ID"
    )
    public ResponseEntity<ApiResponse<JendoReportResponseDto>> getReportById(
            @PathVariable Long id) {
        JendoReportResponseDto report = jendoReportService.getReportById(id);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @GetMapping("/{id}/download")
    @Operation(
        summary = "Download report PDF",
        description = "Downloads the PDF file for a Jendo report"
    )
    public ResponseEntity<Resource> downloadReport(@PathVariable Long id) {
        JendoReportResponseDto reportDto = jendoReportService.getReportById(id);
        Resource resource = jendoReportService.downloadReport(id);
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                    "attachment; filename=\"" + reportDto.getOriginalFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete report",
        description = "Deletes a Jendo report by ID"
    )
    public ResponseEntity<ApiResponse<Void>> deleteReport(@PathVariable Long id) {
        jendoReportService.deleteReport(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Jendo report deleted successfully"));
    }
}
