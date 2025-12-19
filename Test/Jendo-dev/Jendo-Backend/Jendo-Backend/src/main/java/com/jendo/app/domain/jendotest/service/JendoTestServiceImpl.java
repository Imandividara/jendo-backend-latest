package com.jendo.app.domain.jendotest.service;

import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.common.exceptions.NotFoundException;
import com.jendo.app.domain.jendotest.dto.JendoTestRequestDto;
import com.jendo.app.domain.jendotest.dto.JendoTestResponseDto;
import com.jendo.app.domain.jendotest.entity.JendoTest;
import com.jendo.app.domain.jendotest.mapper.JendoTestMapper;
import com.jendo.app.domain.jendotest.repository.JendoTestRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class JendoTestServiceImpl implements JendoTestService {

    private static final Logger logger = LoggerFactory.getLogger(JendoTestServiceImpl.class);
    
    private final JendoTestRepository jendoTestRepository;
    private final UserRepository userRepository;
    private final JendoTestMapper jendoTestMapper;

    @Override
    public JendoTestResponseDto createTest(JendoTestRequestDto request) {
        logger.info("Creating new Jendo test for user ID: {}", request.getUserId());
        
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User", request.getUserId()));
        
        JendoTest test = jendoTestMapper.toEntity(request, user);
        test = jendoTestRepository.save(test);
        
        logger.info("Jendo test created successfully with ID: {}", test.getId());
        return jendoTestMapper.toResponseDto(test);
    }

    @Override
    @Transactional(readOnly = true)
    public JendoTestResponseDto getTestById(Long id) {
        logger.info("Fetching Jendo test with ID: {}", id);
        JendoTest test = jendoTestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("JendoTest", id));
        return jendoTestMapper.toResponseDto(test);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<JendoTestResponseDto> getAllTests(int page, int size) {
        logger.info("Fetching all Jendo tests - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<JendoTest> testPage = jendoTestRepository.findAll(pageable);
        return buildPaginationResponse(testPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<JendoTestResponseDto> getTestsByUserId(Long userId, int page, int size) {
        logger.info("Fetching Jendo tests for user ID: {} - page: {}, size: {}", userId, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<JendoTest> testPage = jendoTestRepository.findByUserId(userId, pageable);
        return buildPaginationResponse(testPage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JendoTestResponseDto> getTestsByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching Jendo tests for user ID: {} between {} and {}", userId, startDate, endDate);
        List<JendoTest> tests = jendoTestRepository.findByUserIdAndTestDateBetween(userId, startDate, endDate);
        return tests.stream().map(jendoTestMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public JendoTestResponseDto updateTest(Long id, JendoTestRequestDto request) {
        logger.info("Updating Jendo test with ID: {}", id);
        
        JendoTest test = jendoTestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("JendoTest", id));
        
        if (request.getUserId() != null && !request.getUserId().equals(test.getUser().getId())) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new NotFoundException("User", request.getUserId()));
            test.setUser(user);
        }
        
        if (request.getScore() != null) test.setScore(request.getScore());
        if (request.getHeartRate() != null) test.setHeartRate(request.getHeartRate());
        if (request.getRiskLevel() != null) test.setRiskLevel(request.getRiskLevel());
        if (request.getTestTime() != null) test.setTestTime(request.getTestTime());
        if (request.getBloodPressure() != null) test.setBloodPressure(request.getBloodPressure());
        if (request.getTestDate() != null) test.setTestDate(request.getTestDate());
        
        test = jendoTestRepository.save(test);
        logger.info("Jendo test updated successfully with ID: {}", id);
        return jendoTestMapper.toResponseDto(test);
    }

    @Override
    public void deleteTest(Long id) {
        logger.info("Deleting Jendo test with ID: {}", id);
        
        if (!jendoTestRepository.existsById(id)) {
            throw new NotFoundException("JendoTest", id);
        }
        
        jendoTestRepository.deleteById(id);
        logger.info("Jendo test deleted successfully with ID: {}", id);
    }
    
    private PaginationResponse<JendoTestResponseDto> buildPaginationResponse(Page<JendoTest> testPage) {
        List<JendoTestResponseDto> content = testPage.getContent().stream()
                .map(jendoTestMapper::toResponseDto)
                .collect(Collectors.toList());
        
        return PaginationResponse.<JendoTestResponseDto>builder()
                .content(content)
                .pageNumber(testPage.getNumber())
                .pageSize(testPage.getSize())
                .totalElements(testPage.getTotalElements())
                .totalPages(testPage.getTotalPages())
                .first(testPage.isFirst())
                .last(testPage.isLast())
                .build();
    }
}
