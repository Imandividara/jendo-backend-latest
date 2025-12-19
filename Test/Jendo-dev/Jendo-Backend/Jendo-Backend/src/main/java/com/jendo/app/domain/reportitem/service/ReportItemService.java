package com.jendo.app.domain.reportitem.service;

import com.jendo.app.domain.reportitem.dto.ReportItemRequestDto;
import com.jendo.app.domain.reportitem.dto.ReportItemResponseDto;

import java.util.List;

public interface ReportItemService {
    List<ReportItemResponseDto> getAllItems();
    List<ReportItemResponseDto> getItemsBySectionId(Long sectionId);
    ReportItemResponseDto getItemById(Long id);
    ReportItemResponseDto createItem(ReportItemRequestDto request);
    ReportItemResponseDto updateItem(Long id, ReportItemRequestDto request);
    void deleteItem(Long id);
}
