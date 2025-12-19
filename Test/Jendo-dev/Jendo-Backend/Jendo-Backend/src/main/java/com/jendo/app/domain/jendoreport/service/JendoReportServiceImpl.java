package com.jendo.app.domain.jendoreport.service;

import com.jendo.app.common.exceptions.NotFoundException;
import com.jendo.app.domain.jendoreport.dto.JendoReportResponseDto;
import com.jendo.app.domain.jendoreport.entity.JendoReport;
import com.jendo.app.domain.jendoreport.repository.JendoReportRepository;
import com.jendo.app.domain.user.entity.User;
import com.jendo.app.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class JendoReportServiceImpl implements JendoReportService {

    private static final Logger logger = LoggerFactory.getLogger(JendoReportServiceImpl.class);

    private final JendoReportRepository jendoReportRepository;
    private final UserRepository userRepository;

    @Value("${jendo.upload.path:uploads/jendo-reports}")
    private String uploadPath;

    @Override
    public JendoReportResponseDto uploadReport(Long userId, MultipartFile file, String description) {
        logger.info("Uploading Jendo report for user ID: {}", userId);

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));

        try {
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String originalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
            Path filePath = uploadDir.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            JendoReport report = JendoReport.builder()
                    .fileName(fileName)
                    .originalFileName(originalFileName)
                    .filePath(filePath.toString())
                    .fileSize(file.getSize())
                    .contentType(contentType)
                    .description(description)
                    .user(user)
                    .build();

            report = jendoReportRepository.save(report);
            logger.info("Jendo report uploaded successfully with ID: {}", report.getId());

            return toResponseDto(report);

        } catch (IOException e) {
            logger.error("Failed to upload file", e);
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<JendoReportResponseDto> getReportsByUserId(Long userId) {
        logger.info("Fetching Jendo reports for user ID: {}", userId);
        return jendoReportRepository.findByUserIdOrderByUploadedAtDesc(userId)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public JendoReportResponseDto getReportById(Long id) {
        logger.info("Fetching Jendo report with ID: {}", id);
        JendoReport report = jendoReportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("JendoReport", id));
        return toResponseDto(report);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource downloadReport(Long id) {
        logger.info("Downloading Jendo report with ID: {}", id);
        JendoReport report = jendoReportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("JendoReport", id));

        try {
            Path filePath = Paths.get(report.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or not readable");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error reading file: " + e.getMessage());
        }
    }

    @Override
    public void deleteReport(Long id) {
        logger.info("Deleting Jendo report with ID: {}", id);
        JendoReport report = jendoReportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("JendoReport", id));

        try {
            Path filePath = Paths.get(report.getFilePath());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            logger.warn("Could not delete file: {}", e.getMessage());
        }

        jendoReportRepository.delete(report);
        logger.info("Jendo report deleted successfully");
    }

    private JendoReportResponseDto toResponseDto(JendoReport report) {
        return JendoReportResponseDto.builder()
                .id(report.getId())
                .userId(report.getUser().getId())
                .fileName(report.getFileName())
                .originalFileName(report.getOriginalFileName())
                .fileSize(report.getFileSize())
                .contentType(report.getContentType())
                .description(report.getDescription())
                .uploadedAt(report.getUploadedAt())
                .downloadUrl("/api/jendo-reports/" + report.getId() + "/download")
                .build();
    }
}
