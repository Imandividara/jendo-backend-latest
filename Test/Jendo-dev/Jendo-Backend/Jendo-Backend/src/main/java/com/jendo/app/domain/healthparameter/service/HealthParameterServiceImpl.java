package com.jendo.app.domain.healthparameter.service;

import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.common.exceptions.NotFoundException;
import com.jendo.app.domain.healthparameter.dto.HealthParameterRequestDto;
import com.jendo.app.domain.healthparameter.dto.HealthParameterResponseDto;
import com.jendo.app.domain.healthparameter.entity.HealthParameter;
import com.jendo.app.domain.healthparameter.mapper.HealthParameterMapper;
import com.jendo.app.domain.healthparameter.repository.HealthParameterRepository;
import com.jendo.app.domain.user.entity.User;
import com.jendo.app.domain.user.repository.UserRepository;
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
public class HealthParameterServiceImpl implements HealthParameterService {

    private static final Logger logger = LoggerFactory.getLogger(HealthParameterServiceImpl.class);
    
    private final HealthParameterRepository healthParameterRepository;
    private final UserRepository userRepository;
    private final HealthParameterMapper healthParameterMapper;

    @Override
    public HealthParameterResponseDto createHealthParameter(HealthParameterRequestDto request) {
        logger.info("Creating health parameter for user ID: {}", request.getUserId());
        
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User", request.getUserId()));
        
        HealthParameter healthParameter = healthParameterMapper.toEntity(request, user);
        healthParameter = healthParameterRepository.save(healthParameter);
        
        logger.info("Health parameter created with ID: {}", healthParameter.getId());
        return healthParameterMapper.toResponseDto(healthParameter);
    }

    @Override
    @Transactional(readOnly = true)
    public HealthParameterResponseDto getHealthParameterById(Long id) {
        logger.info("Fetching health parameter with ID: {}", id);
        HealthParameter healthParameter = healthParameterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("HealthParameter", id));
        return healthParameterMapper.toResponseDto(healthParameter);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<HealthParameterResponseDto> getHealthParametersByUserId(Long userId, int page, int size) {
        logger.info("Fetching health parameters for user ID: {} - page: {}, size: {}", userId, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<HealthParameter> parameterPage = healthParameterRepository.findByUserId(userId, pageable);
        
        List<HealthParameterResponseDto> content = parameterPage.getContent().stream()
                .map(healthParameterMapper::toResponseDto)
                .collect(Collectors.toList());
        
        return PaginationResponse.<HealthParameterResponseDto>builder()
                .content(content)
                .pageNumber(parameterPage.getNumber())
                .pageSize(parameterPage.getSize())
                .totalElements(parameterPage.getTotalElements())
                .totalPages(parameterPage.getTotalPages())
                .first(parameterPage.isFirst())
                .last(parameterPage.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public HealthParameterResponseDto getLatestHealthParameterByUserId(Long userId) {
        logger.info("Fetching latest health parameter for user ID: {}", userId);
        return healthParameterRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .map(healthParameterMapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException("No health parameters found for user: " + userId));
    }

    @Override
    public HealthParameterResponseDto updateHealthParameter(Long id, HealthParameterRequestDto request) {
        logger.info("Updating health parameter with ID: {}", id);
        
        HealthParameter healthParameter = healthParameterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("HealthParameter", id));
        
        if (request.getBloodType() != null) healthParameter.setBloodType(request.getBloodType());
        if (request.getHeight() != null) healthParameter.setHeight(request.getHeight());
        if (request.getWeight() != null) healthParameter.setWeight(request.getWeight());
        if (request.getBmi() != null) healthParameter.setBmi(request.getBmi());
        
        healthParameter = healthParameterRepository.save(healthParameter);
        logger.info("Health parameter updated with ID: {}", id);
        return healthParameterMapper.toResponseDto(healthParameter);
    }

    @Override
    public void deleteHealthParameter(Long id) {
        logger.info("Deleting health parameter with ID: {}", id);
        if (!healthParameterRepository.existsById(id)) {
            throw new NotFoundException("HealthParameter", id);
        }
        healthParameterRepository.deleteById(id);
    }
}
