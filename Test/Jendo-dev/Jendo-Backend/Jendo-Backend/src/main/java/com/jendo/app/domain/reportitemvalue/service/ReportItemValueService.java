package com.jendo.app.domain.reportitemvalue.service;

import com.jendo.app.domain.reportitemvalue.dto.ReportItemValueRequestDto;
import com.jendo.app.domain.reportitemvalue.dto.ReportItemValueResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReportItemValueService {
    List<ReportItemValueResponseDto> getAllValues();
    List<ReportItemValueResponseDto> getValuesByUserId(Long userId);
    List<ReportItemValueResponseDto> getValuesByReportItemId(Long reportItemId);
    List<ReportItemValueResponseDto> getValuesByUserIdAndReportItemId(Long userId, Long reportItemId);
    ReportItemValueResponseDto getValueById(Long id);
    ReportItemValueResponseDto createValue(ReportItemValueRequestDto request, String userEmail);
    ReportItemValueResponseDto updateValue(Long id, ReportItemValueRequestDto request, String userEmail);
    void deleteValue(Long id);
    ReportItemValueResponseDto addAttachment(Long valueId, MultipartFile file, String userEmail);
    void deleteAttachment(Long attachmentId);
    Resource downloadAttachment(Long attachmentId);
}
