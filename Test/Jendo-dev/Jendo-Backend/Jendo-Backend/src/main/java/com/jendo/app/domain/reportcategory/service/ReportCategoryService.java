package com.jendo.app.domain.reportcategory.service;

import com.jendo.app.domain.reportcategory.dto.ReportCategoryRequestDto;
import com.jendo.app.domain.reportcategory.dto.ReportCategoryResponseDto;

import java.util.List;

public interface ReportCategoryService {
    List<ReportCategoryResponseDto> getAllCategories();
    ReportCategoryResponseDto getCategoryById(Long id);
    ReportCategoryResponseDto getCategoryWithSections(Long id);
    ReportCategoryResponseDto createCategory(ReportCategoryRequestDto request);
    ReportCategoryResponseDto updateCategory(Long id, ReportCategoryRequestDto request);
    void deleteCategory(Long id);
}
