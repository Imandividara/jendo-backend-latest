package com.jendo.app.domain.jendotest.service;

import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.domain.jendotest.dto.JendoTestRequestDto;
import com.jendo.app.domain.jendotest.dto.JendoTestResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface JendoTestService {
    
    JendoTestResponseDto createTest(JendoTestRequestDto request);
    
    JendoTestResponseDto getTestById(Long id);
    
    PaginationResponse<JendoTestResponseDto> getAllTests(int page, int size);
    
    PaginationResponse<JendoTestResponseDto> getTestsByUserId(Long userId, int page, int size);
    
    List<JendoTestResponseDto> getTestsByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);
    
    JendoTestResponseDto updateTest(Long id, JendoTestRequestDto request);
    
    void deleteTest(Long id);
}
