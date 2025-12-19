package com.jendo.app.domain.wellnessrecommendation.service;

import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.common.exceptions.NotFoundException;
import com.jendo.app.domain.wellnessrecommendation.dto.WellnessRecommendationDto;
import com.jendo.app.domain.wellnessrecommendation.dto.WellnessRecommendationRequestDto;
import com.jendo.app.domain.wellnessrecommendation.entity.WellnessRecommendation;
import com.jendo.app.domain.wellnessrecommendation.repository.WellnessRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WellnessRecommendationServiceImpl implements WellnessRecommendationService {

    private final WellnessRecommendationRepository repository;

    @Override
    public WellnessRecommendationDto create(WellnessRecommendationRequestDto request) {
        WellnessRecommendation entity = WellnessRecommendation.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .riskLevel(request.getRiskLevel())
                .type(request.getType())
                .priority(request.getPriority() != null ? request.getPriority() : 0)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
        
        WellnessRecommendation saved = repository.save(entity);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public WellnessRecommendationDto getById(Long id) {
        WellnessRecommendation entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Wellness recommendation not found with id: " + id));
        return toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<WellnessRecommendationDto> getAll(int page, int size) {
        Page<WellnessRecommendation> pageResult = repository.findAll(
                PageRequest.of(page, size, Sort.by("priority").ascending().and(Sort.by("id").descending()))
        );
        
        List<WellnessRecommendationDto> content = pageResult.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        
        return PaginationResponse.<WellnessRecommendationDto>builder()
                .content(content)
                .pageNumber(page)
                .pageSize(size)
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .first(pageResult.isFirst())
                .last(pageResult.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<WellnessRecommendationDto> getByRiskLevel(String riskLevel) {
        List<WellnessRecommendation> recommendations = 
                repository.findByRiskLevelIgnoreCaseAndIsActiveTrueOrderByPriorityAsc(riskLevel);
        return recommendations.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public WellnessRecommendationDto update(Long id, WellnessRecommendationRequestDto request) {
        WellnessRecommendation entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Wellness recommendation not found with id: " + id));
        
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setCategory(request.getCategory());
        entity.setRiskLevel(request.getRiskLevel());
        entity.setType(request.getType());
        if (request.getPriority() != null) {
            entity.setPriority(request.getPriority());
        }
        if (request.getIsActive() != null) {
            entity.setIsActive(request.getIsActive());
        }
        
        WellnessRecommendation updated = repository.save(entity);
        return toDto(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Wellness recommendation not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private WellnessRecommendationDto toDto(WellnessRecommendation entity) {
        return WellnessRecommendationDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .riskLevel(entity.getRiskLevel())
                .type(entity.getType())
                .priority(entity.getPriority())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
