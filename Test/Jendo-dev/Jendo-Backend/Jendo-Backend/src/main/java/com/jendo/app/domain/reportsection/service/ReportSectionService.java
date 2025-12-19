package com.jendo.app.domain.reportsection.service;

import com.jendo.app.domain.reportsection.dto.ReportSectionRequestDto;
import com.jendo.app.domain.reportsection.dto.ReportSectionResponseDto;

import java.util.List;

public interface ReportSectionService {
    List<ReportSectionResponseDto> getAllSections();
    List<ReportSectionResponseDto> getSectionsByCategoryId(Long categoryId);
    ReportSectionResponseDto getSectionById(Long id);
    ReportSectionResponseDto getSectionWithItems(Long id);
    ReportSectionResponseDto createSection(ReportSectionRequestDto request);
    ReportSectionResponseDto updateSection(Long id, ReportSectionRequestDto request);
    void deleteSection(Long id);
}
