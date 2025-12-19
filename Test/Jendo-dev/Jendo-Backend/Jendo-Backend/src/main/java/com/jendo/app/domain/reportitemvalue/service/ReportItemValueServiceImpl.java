package com.jendo.app.domain.reportitemvalue.service;

import com.jendo.app.common.exceptions.NotFoundException;
import com.jendo.app.domain.reportattachment.dto.ReportAttachmentResponseDto;
import com.jendo.app.domain.reportattachment.entity.ReportAttachment;
import com.jendo.app.domain.reportattachment.repository.ReportAttachmentRepository;
import com.jendo.app.domain.reportitem.entity.ReportItem;
import com.jendo.app.domain.reportitem.repository.ReportItemRepository;
import com.jendo.app.domain.reportitemvalue.dto.ReportItemValueRequestDto;
import com.jendo.app.domain.reportitemvalue.dto.ReportItemValueResponseDto;
import com.jendo.app.domain.reportitemvalue.entity.ReportItemValue;
import com.jendo.app.domain.reportitemvalue.repository.ReportItemValueRepository;
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
public class ReportItemValueServiceImpl implements ReportItemValueService {

    private static final Logger logger = LoggerFactory.getLogger(ReportItemValueServiceImpl.class);

    private final ReportItemValueRepository repository;
    private final ReportItemRepository reportItemRepository;
    private final ReportAttachmentRepository attachmentRepository;
    private final UserRepository userRepository;

    @Value("${jendo.upload.attachments.path:uploads/report-attachments}")
    private String uploadPath;

    @Override
    @Transactional(readOnly = true)
    public List<ReportItemValueResponseDto> getAllValues() {
        return repository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportItemValueResponseDto> getValuesByUserId(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportItemValueResponseDto> getValuesByReportItemId(Long reportItemId) {
        return repository.findByReportItemId(reportItemId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportItemValueResponseDto> getValuesByUserIdAndReportItemId(Long userId, Long reportItemId) {
        return repository.findByUserIdAndReportItemId(userId, reportItemId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReportItemValueResponseDto getValueById(Long id) {
        return toResponseDto(findById(id));
    }

    @Override
    public ReportItemValueResponseDto createValue(ReportItemValueRequestDto request, String userEmail) {
        ReportItem reportItem = reportItemRepository.findById(request.getReportItemId())
                .orElseThrow(() -> new NotFoundException("ReportItem", request.getReportItemId()));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));

        ReportItemValue value = ReportItemValue.builder()
                .reportItem(reportItem)
                .valueNumber(request.getValueNumber())
                .valueText(request.getValueText())
                .valueDate(request.getValueDate())
                .user(user)
                .build();

        return toResponseDto(repository.save(value));
    }

    @Override
    public ReportItemValueResponseDto updateValue(Long id, ReportItemValueRequestDto request, String userEmail) {
        ReportItemValue value = findById(id);
        
        // Verify user owns this value
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));
        
        if (value.getUser() == null || !value.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized: You can only update your own reports");
        }
        
        value.setValueNumber(request.getValueNumber());
        value.setValueText(request.getValueText());
        value.setValueDate(request.getValueDate());

        if (request.getReportItemId() != null && !request.getReportItemId().equals(value.getReportItem().getId())) {
            ReportItem reportItem = reportItemRepository.findById(request.getReportItemId())
                    .orElseThrow(() -> new NotFoundException("ReportItem", request.getReportItemId()));
            value.setReportItem(reportItem);
        }

        return toResponseDto(repository.save(value));
    }

    @Override
    public void deleteValue(Long id) {
        ReportItemValue value = findById(id);
        for (ReportAttachment attachment : value.getReportAttachments()) {
            try {
                Path filePath = Paths.get(attachment.getFileUrl());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                logger.warn("Could not delete attachment file: {}", e.getMessage());
            }
        }
        repository.delete(value);
    }

    @Override
    public ReportItemValueResponseDto addAttachment(Long valueId, MultipartFile file, String userEmail) {
        logger.info("Adding attachment to value ID: {}", valueId);

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        ReportItemValue value = findById(valueId);
        
        // Verify user owns this value
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));
        
        if (value.getUser() == null || !value.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized: You can only add attachments to your own reports");
        }

        try {
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String originalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
            Path filePath = uploadDir.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            ReportAttachment attachment = ReportAttachment.builder()
                    .fileUrl(filePath.toString())
                    .fileType(file.getContentType())
                    .reportItemValue(value)
                    .build();

            value.getReportAttachments().add(attachment);
            repository.save(value);

            logger.info("Attachment added successfully");
            return toResponseDto(value);

        } catch (IOException e) {
            logger.error("Failed to upload attachment", e);
            throw new RuntimeException("Failed to upload attachment: " + e.getMessage());
        }
    }

    @Override
    public void deleteAttachment(Long attachmentId) {
        logger.info("Deleting attachment ID: {}", attachmentId);

        ReportAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new NotFoundException("ReportAttachment", attachmentId));

        try {
            Path filePath = Paths.get(attachment.getFileUrl());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            logger.warn("Could not delete attachment file: {}", e.getMessage());
        }

        attachmentRepository.delete(attachment);
        logger.info("Attachment deleted successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public Resource downloadAttachment(Long attachmentId) {
        logger.info("Downloading attachment ID: {}", attachmentId);

        ReportAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new NotFoundException("ReportAttachment", attachmentId));

        try {
            Path filePath = Paths.get(attachment.getFileUrl());
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

    private ReportItemValue findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("ReportItemValue", id));
    }

    private ReportItemValueResponseDto toResponseDto(ReportItemValue value) {
        List<ReportAttachmentResponseDto> attachments = value.getReportAttachments().stream()
                .map(a -> ReportAttachmentResponseDto.builder()
                        .id(a.getId())
                        .fileUrl(a.getFileUrl())
                        .fileType(a.getFileType())
                        .uploadedAt(a.getUploadedAt())
                        .reportItemValueId(value.getId())
                        .downloadUrl("/api/report-values/attachments/" + a.getId() + "/download")
                        .build())
                .collect(Collectors.toList());

        return ReportItemValueResponseDto.builder()
                .id(value.getId())
                .reportItemId(value.getReportItem().getId())
                .reportItemName(value.getReportItem().getName())
                .userId(value.getUser() != null ? value.getUser().getId() : null)
                .valueNumber(value.getValueNumber())
                .valueText(value.getValueText())
                .valueDate(value.getValueDate())
                .createdAt(value.getCreatedAt())
                .updatedAt(value.getUpdatedAt())
                .attachments(attachments)
                .build();
    }
}
