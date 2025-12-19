package com.jendo.app.domain.reportitem.service;

import com.jendo.app.common.exceptions.NotFoundException;
import com.jendo.app.domain.reportitem.dto.ReportItemRequestDto;
import com.jendo.app.domain.reportitem.dto.ReportItemResponseDto;
import com.jendo.app.domain.reportitem.entity.ReportItem;
import com.jendo.app.domain.reportitem.repository.ReportItemRepository;
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
public class ReportItemServiceImpl implements ReportItemService {

    private final ReportItemRepository repository;
    private final ReportSectionRepository sectionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ReportItemResponseDto> getAllItems() {
        return repository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportItemResponseDto> getItemsBySectionId(Long sectionId) {
        return repository.findBySectionId(sectionId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReportItemResponseDto getItemById(Long id) {
        return toResponseDto(findById(id));
    }

    @Override
    public ReportItemResponseDto createItem(ReportItemRequestDto request) {
        ReportItem item = ReportItem.builder()
                .name(request.getName())
                .description(request.getDescription())
                .icon(request.getIcon())
                .build();

        if (request.getSectionId() != null) {
            ReportSection section = sectionRepository.findById(request.getSectionId())
                    .orElseThrow(() -> new NotFoundException("ReportSection", request.getSectionId()));
            item.setReportSection(section);
        }

        return toResponseDto(repository.save(item));
    }

    @Override
    public ReportItemResponseDto updateItem(Long id, ReportItemRequestDto request) {
        ReportItem item = findById(id);
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setIcon(request.getIcon());

        if (request.getSectionId() != null) {
            ReportSection section = sectionRepository.findById(request.getSectionId())
                    .orElseThrow(() -> new NotFoundException("ReportSection", request.getSectionId()));
            item.setReportSection(section);
        } else {
            item.setReportSection(null);
        }

        return toResponseDto(repository.save(item));
    }

    @Override
    public void deleteItem(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("ReportItem", id);
        }
        repository.deleteById(id);
    }

    private ReportItem findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("ReportItem", id));
    }

    private ReportItemResponseDto toResponseDto(ReportItem item) {
        return ReportItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .icon(item.getIcon())
                .sectionId(item.getReportSection() != null ? item.getReportSection().getId() : null)
                .sectionName(item.getReportSection() != null ? item.getReportSection().getName() : null)
                .build();
    }
}
