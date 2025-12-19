package com.jendo.app.domain.reportsection.service;

import com.jendo.app.common.exceptions.NotFoundException;
import com.jendo.app.domain.reportcategory.entity.ReportCategory;
import com.jendo.app.domain.reportcategory.repository.ReportCategoryRepository;
import com.jendo.app.domain.reportitem.dto.ReportItemResponseDto;
import com.jendo.app.domain.reportsection.dto.ReportSectionRequestDto;
import com.jendo.app.domain.reportsection.dto.ReportSectionResponseDto;
import com.jendo.app.domain.reportsection.entity.ReportSection;
import com.jendo.app.domain.reportsection.repository.ReportSectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportSectionServiceImpl implements ReportSectionService {

    private final ReportSectionRepository repository;
    private final ReportCategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ReportSectionResponseDto> getAllSections() {
        return repository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportSectionResponseDto> getSectionsByCategoryId(Long categoryId) {
        return repository.findByCategoryId(categoryId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReportSectionResponseDto getSectionById(Long id) {
        return toResponseDto(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ReportSectionResponseDto getSectionWithItems(Long id) {
        ReportSection section = findById(id);
        return toResponseDtoWithItems(section);
    }

    @Override
    public ReportSectionResponseDto createSection(ReportSectionRequestDto request) {
        ReportSection section = ReportSection.builder()
                .name(request.getName())
                .icon(request.getIcon())
                .description(request.getDescription())
                .build();

        if (request.getCategoryId() != null) {
            ReportCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("ReportCategory", request.getCategoryId()));
            section.setReportCategory(category);
        }

        return toResponseDto(repository.save(section));
    }

    @Override
    public ReportSectionResponseDto updateSection(Long id, ReportSectionRequestDto request) {
        ReportSection section = findById(id);
        section.setName(request.getName());
        section.setIcon(request.getIcon());
        section.setDescription(request.getDescription());

        if (request.getCategoryId() != null) {
            ReportCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("ReportCategory", request.getCategoryId()));
            section.setReportCategory(category);
        } else {
            section.setReportCategory(null);
        }

        return toResponseDto(repository.save(section));
    }

    @Override
    public void deleteSection(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("ReportSection", id);
        }
        repository.deleteById(id);
    }

    private ReportSection findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("ReportSection", id));
    }

    private ReportSectionResponseDto toResponseDto(ReportSection section) {
        return ReportSectionResponseDto.builder()
                .id(section.getId())
                .name(section.getName())
                .icon(section.getIcon())
                .description(section.getDescription())
                .categoryId(section.getReportCategory() != null ? section.getReportCategory().getId() : null)
                .categoryName(section.getReportCategory() != null ? section.getReportCategory().getName() : null)
                .build();
    }

    private ReportSectionResponseDto toResponseDtoWithItems(ReportSection section) {
        List<ReportItemResponseDto> items = section.getReportItems().stream()
                .map(item -> ReportItemResponseDto.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .description(item.getDescription())
                        .icon(item.getIcon())
                        .sectionId(section.getId())
                        .sectionName(section.getName())
                        .build())
                .collect(Collectors.toList());

        return ReportSectionResponseDto.builder()
                .id(section.getId())
                .name(section.getName())
                .icon(section.getIcon())
                .description(section.getDescription())
                .categoryId(section.getReportCategory() != null ? section.getReportCategory().getId() : null)
                .categoryName(section.getReportCategory() != null ? section.getReportCategory().getName() : null)
                .items(items)
                .build();
    }
}
