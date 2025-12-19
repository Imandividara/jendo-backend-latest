package com.jendo.app.domain.learningmaterial.service;

import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.common.exceptions.NotFoundException;
import com.jendo.app.domain.learningmaterial.dto.LearningMaterialRequestDto;
import com.jendo.app.domain.learningmaterial.dto.LearningMaterialResponseDto;
import com.jendo.app.domain.learningmaterial.entity.LearningMaterial;
import com.jendo.app.domain.learningmaterial.mapper.LearningMaterialMapper;
import com.jendo.app.domain.learningmaterial.repository.LearningMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LearningMaterialServiceImpl implements LearningMaterialService {

    private static final Logger logger = LoggerFactory.getLogger(LearningMaterialServiceImpl.class);
    
    private final LearningMaterialRepository learningMaterialRepository;
    private final LearningMaterialMapper learningMaterialMapper;

    @Override
    public LearningMaterialResponseDto createLearningMaterial(LearningMaterialRequestDto request) {
        logger.info("Creating learning material: {}", request.getTitle());
        
        LearningMaterial material = learningMaterialMapper.toEntity(request);
        material = learningMaterialRepository.save(material);
        
        logger.info("Learning material created with ID: {}", material.getId());
        return learningMaterialMapper.toResponseDto(material);
    }

    @Override
    @Transactional(readOnly = true)
    public LearningMaterialResponseDto getLearningMaterialById(Long id) {
        logger.info("Fetching learning material with ID: {}", id);
        LearningMaterial material = learningMaterialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("LearningMaterial", id));
        return learningMaterialMapper.toResponseDto(material);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<LearningMaterialResponseDto> getAllLearningMaterials(int page, int size) {
        logger.info("Fetching all learning materials - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<LearningMaterial> materialPage = learningMaterialRepository.findAll(pageable);
        return buildPaginationResponse(materialPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<LearningMaterialResponseDto> getLearningMaterialsByCategory(String category, int page, int size) {
        logger.info("Fetching learning materials by category: {} - page: {}, size: {}", category, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<LearningMaterial> materialPage = learningMaterialRepository.findByCategory(category, pageable);
        return buildPaginationResponse(materialPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<LearningMaterialResponseDto> searchLearningMaterials(String query, int page, int size) {
        logger.info("Searching learning materials with query: {} - page: {}, size: {}", query, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<LearningMaterial> materialPage = learningMaterialRepository.findByTitleContainingIgnoreCase(query, pageable);
        return buildPaginationResponse(materialPage);
    }

    @Override
    public LearningMaterialResponseDto updateLearningMaterial(Long id, LearningMaterialRequestDto request) {
        logger.info("Updating learning material with ID: {}", id);
        
        LearningMaterial material = learningMaterialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("LearningMaterial", id));
        
        if (request.getTitle() != null) material.setTitle(request.getTitle());
        if (request.getAuthor() != null) material.setAuthor(request.getAuthor());
        if (request.getDuration() != null) material.setDuration(request.getDuration());
        if (request.getDescription() != null) material.setDescription(request.getDescription());
        if (request.getType() != null) material.setType(request.getType());
        if (request.getVideoUrl() != null) material.setVideoUrl(request.getVideoUrl());
        if (request.getCategory() != null) material.setCategory(request.getCategory());
        
        material = learningMaterialRepository.save(material);
        logger.info("Learning material updated with ID: {}", id);
        return learningMaterialMapper.toResponseDto(material);
    }

    @Override
    public void deleteLearningMaterial(Long id) {
        logger.info("Deleting learning material with ID: {}", id);
        if (!learningMaterialRepository.existsById(id)) {
            throw new NotFoundException("LearningMaterial", id);
        }
        learningMaterialRepository.deleteById(id);
    }
    
    private PaginationResponse<LearningMaterialResponseDto> buildPaginationResponse(Page<LearningMaterial> materialPage) {
        List<LearningMaterialResponseDto> content = materialPage.getContent().stream()
                .map(learningMaterialMapper::toResponseDto)
                .collect(Collectors.toList());
        
        return PaginationResponse.<LearningMaterialResponseDto>builder()
                .content(content)
                .pageNumber(materialPage.getNumber())
                .pageSize(materialPage.getSize())
                .totalElements(materialPage.getTotalElements())
                .totalPages(materialPage.getTotalPages())
                .first(materialPage.isFirst())
                .last(materialPage.isLast())
                .build();
    }
}
