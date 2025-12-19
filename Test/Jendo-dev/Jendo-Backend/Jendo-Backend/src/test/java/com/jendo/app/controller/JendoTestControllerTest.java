package com.jendo.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.domain.jendotest.dto.JendoTestRequestDto;
import com.jendo.app.domain.jendotest.dto.JendoTestResponseDto;
import com.jendo.app.domain.jendotest.service.JendoTestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JendoTestController.class)
class JendoTestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JendoTestService jendoTestService;

    private JendoTestRequestDto testRequest;
    private JendoTestResponseDto testResponse;

    @BeforeEach
    void setUp() {
        testRequest = JendoTestRequestDto.builder()
                .userId(1L)
                .score(new BigDecimal("85.5"))
                .heartRate(72)
                .riskLevel("LOW")
                .testTime(LocalTime.of(14, 30))
                .bloodPressure("120/80")
                .testDate(LocalDate.now())
                .build();

        testResponse = JendoTestResponseDto.builder()
                .id(1L)
                .userId(1L)
                .userName("John Doe")
                .score(new BigDecimal("85.5"))
                .heartRate(72)
                .riskLevel("LOW")
                .testTime(LocalTime.of(14, 30))
                .bloodPressure("120/80")
                .testDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Create Jendo test - Success")
    void createTest_Success() throws Exception {
        when(jendoTestService.createTest(any(JendoTestRequestDto.class))).thenReturn(testResponse);

        mockMvc.perform(post("/api/jendo-tests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.riskLevel").value("LOW"));

        verify(jendoTestService, times(1)).createTest(any(JendoTestRequestDto.class));
    }

    @Test
    @DisplayName("Create Jendo test - Validation error for null userId")
    void createTest_ValidationError_NullUserId() throws Exception {
        testRequest.setUserId(null);

        mockMvc.perform(post("/api/jendo-tests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get test by ID - Success")
    void getTestById_Success() throws Exception {
        when(jendoTestService.getTestById(1L)).thenReturn(testResponse);

        mockMvc.perform(get("/api/jendo-tests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(jendoTestService, times(1)).getTestById(1L);
    }

    @Test
    @DisplayName("Get all tests - Success")
    void getAllTests_Success() throws Exception {
        PaginationResponse<JendoTestResponseDto> paginationResponse = PaginationResponse.<JendoTestResponseDto>builder()
                .content(List.of(testResponse))
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(jendoTestService.getAllTests(0, 10)).thenReturn(paginationResponse);

        mockMvc.perform(get("/api/jendo-tests")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray());

        verify(jendoTestService, times(1)).getAllTests(0, 10);
    }

    @Test
    @DisplayName("Get tests by user ID - Success")
    void getTestsByUserId_Success() throws Exception {
        PaginationResponse<JendoTestResponseDto> paginationResponse = PaginationResponse.<JendoTestResponseDto>builder()
                .content(List.of(testResponse))
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(jendoTestService.getTestsByUserId(1L, 0, 10)).thenReturn(paginationResponse);

        mockMvc.perform(get("/api/jendo-tests/user/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(jendoTestService, times(1)).getTestsByUserId(1L, 0, 10);
    }

    @Test
    @DisplayName("Update test - Success")
    void updateTest_Success() throws Exception {
        when(jendoTestService.updateTest(eq(1L), any(JendoTestRequestDto.class))).thenReturn(testResponse);

        mockMvc.perform(put("/api/jendo-tests/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(jendoTestService, times(1)).updateTest(eq(1L), any(JendoTestRequestDto.class));
    }

    @Test
    @DisplayName("Delete test - Success")
    void deleteTest_Success() throws Exception {
        doNothing().when(jendoTestService).deleteTest(1L);

        mockMvc.perform(delete("/api/jendo-tests/1"))
                .andExpect(status().isNoContent());

        verify(jendoTestService, times(1)).deleteTest(1L);
    }
}
