package com.jendo.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.domain.healthparameter.dto.HealthParameterRequestDto;
import com.jendo.app.domain.healthparameter.dto.HealthParameterResponseDto;
import com.jendo.app.domain.healthparameter.service.HealthParameterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HealthParameterController.class)
class HealthParameterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HealthParameterService healthParameterService;

    private HealthParameterRequestDto healthParameterRequest;
    private HealthParameterResponseDto healthParameterResponse;

    @BeforeEach
    void setUp() {
        healthParameterRequest = HealthParameterRequestDto.builder()
                .userId(1L)
                .bloodType("O+")
                .height(new BigDecimal("175.5"))
                .weight(new BigDecimal("70.5"))
                .bmi(new BigDecimal("22.9"))
                .build();

        healthParameterResponse = HealthParameterResponseDto.builder()
                .id(1L)
                .userId(1L)
                .bloodType("O+")
                .height(new BigDecimal("175.5"))
                .weight(new BigDecimal("70.5"))
                .bmi(new BigDecimal("22.9"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Create health parameter - Success")
    void createHealthParameter_Success() throws Exception {
        when(healthParameterService.createHealthParameter(any(HealthParameterRequestDto.class)))
                .thenReturn(healthParameterResponse);

        mockMvc.perform(post("/api/health-parameters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(healthParameterRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.bloodType").value("O+"));

        verify(healthParameterService, times(1)).createHealthParameter(any(HealthParameterRequestDto.class));
    }

    @Test
    @DisplayName("Create health parameter - Validation error for null userId")
    void createHealthParameter_ValidationError_NullUserId() throws Exception {
        healthParameterRequest.setUserId(null);

        mockMvc.perform(post("/api/health-parameters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(healthParameterRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get health parameter by ID - Success")
    void getHealthParameterById_Success() throws Exception {
        when(healthParameterService.getHealthParameterById(1L)).thenReturn(healthParameterResponse);

        mockMvc.perform(get("/api/health-parameters/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(healthParameterService, times(1)).getHealthParameterById(1L);
    }

    @Test
    @DisplayName("Get health parameters by user - Success")
    void getHealthParametersByUserId_Success() throws Exception {
        List<HealthParameterResponseDto> params = Arrays.asList(healthParameterResponse);
        PaginationResponse<HealthParameterResponseDto> paginationResponse = PaginationResponse.<HealthParameterResponseDto>builder()
                .content(params)
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(healthParameterService.getHealthParametersByUserId(eq(1L), eq(0), eq(10))).thenReturn(paginationResponse);

        mockMvc.perform(get("/api/health-parameters/user/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray());

        verify(healthParameterService, times(1)).getHealthParametersByUserId(eq(1L), eq(0), eq(10));
    }

    @Test
    @DisplayName("Get latest health parameter - Success")
    void getLatestHealthParameter_Success() throws Exception {
        when(healthParameterService.getLatestHealthParameterByUserId(1L)).thenReturn(healthParameterResponse);

        mockMvc.perform(get("/api/health-parameters/user/1/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(healthParameterService, times(1)).getLatestHealthParameterByUserId(1L);
    }

    @Test
    @DisplayName("Update health parameter - Success")
    void updateHealthParameter_Success() throws Exception {
        when(healthParameterService.updateHealthParameter(eq(1L), any(HealthParameterRequestDto.class)))
                .thenReturn(healthParameterResponse);

        mockMvc.perform(put("/api/health-parameters/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(healthParameterRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(healthParameterService, times(1)).updateHealthParameter(eq(1L), any(HealthParameterRequestDto.class));
    }

    @Test
    @DisplayName("Delete health parameter - Success")
    void deleteHealthParameter_Success() throws Exception {
        doNothing().when(healthParameterService).deleteHealthParameter(1L);

        mockMvc.perform(delete("/api/health-parameters/1"))
                .andExpect(status().isNoContent());

        verify(healthParameterService, times(1)).deleteHealthParameter(1L);
    }
}
