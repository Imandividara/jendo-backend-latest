package com.jendo.app.domain.reportcategory.service;

import com.jendo.app.common.exceptions.NotFoundException;
import com.jendo.app.domain.reportcategory.dto.ReportCategoryRequestDto;
import com.jendo.app.domain.reportcategory.dto.ReportCategoryResponseDto;
import com.jendo.app.domain.reportcategory.entity.ReportCategory;
import com.jendo.app.domain.reportcategory.repository.ReportCategoryRepository;
import com.jendo.app.domain.reportsection.dto.ReportSectionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportCategoryServiceImpl implements ReportCategoryService {

    private final ReportCategoryRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<ReportCategoryResponseDto> getAllCategories() {
        return repository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReportCategoryResponseDto getCategoryById(Long id) {
        return toResponseDto(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ReportCategoryResponseDto getCategoryWithSections(Long id) {
        ReportCategory category = findById(id);
        return toResponseDtoWithSections(category);
    }

    @Override
    public ReportCategoryResponseDto createCategory(ReportCategoryRequestDto request) {
        ReportCategory category = ReportCategory.builder()
                .name(request.getName())
                .icon(request.getIcon())
                .build();
        return toResponseDto(repository.save(category));
    }

    @Override
    public ReportCategoryResponseDto updateCategory(Long id, ReportCategoryRequestDto request) {
        ReportCategory category = findById(id);
        category.setName(request.getName());
        category.setIcon(request.getIcon());
        return toResponseDto(repository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("ReportCategory", id);
        }
        repository.deleteById(id);
    }

    private ReportCategory findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("ReportCategory", id));
    }

    private ReportCategoryResponseDto toResponseDto(ReportCategory category) {
        return ReportCategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .icon(category.getIcon())
                .createdAt(category.getCreatedAt())
                .lastUpdated(category.getLastUpdated())
                .build();
    }

    private ReportCategoryResponseDto toResponseDtoWithSections(ReportCategory category) {
        List<ReportSectionResponseDto> sections = category.getReportSections().stream()
                .map(section -> ReportSectionResponseDto.builder()
                        .id(section.getId())
                        .name(section.getName())
                        .icon(section.getIcon())
                        .description(section.getDescription())
                        .categoryId(category.getId())
                        .categoryName(category.getName())
                        .build())
                .collect(Collectors.toList());

        return ReportCategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .icon(category.getIcon())
                .createdAt(category.getCreatedAt())
                .lastUpdated(category.getLastUpdated())
                .sections(sections)
                .build();
    }
}
