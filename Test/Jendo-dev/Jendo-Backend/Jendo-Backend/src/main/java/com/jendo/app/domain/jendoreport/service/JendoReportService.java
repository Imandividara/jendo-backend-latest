package com.jendo.app.domain.jendoreport.service;

import com.jendo.app.domain.jendoreport.dto.JendoReportResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JendoReportService {
    
    JendoReportResponseDto uploadReport(Long userId, MultipartFile file, String description);
    
    List<JendoReportResponseDto> getReportsByUserId(Long userId);
    
    JendoReportResponseDto getReportById(Long id);
    
    Resource downloadReport(Long id);
    
    void deleteReport(Long id);
}
