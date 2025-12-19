package com.jendo.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.domain.learningmaterial.dto.LearningMaterialRequestDto;
import com.jendo.app.domain.learningmaterial.dto.LearningMaterialResponseDto;
import com.jendo.app.domain.learningmaterial.service.LearningMaterialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LearningMaterialController.class)
class LearningMaterialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LearningMaterialService learningMaterialService;

    private LearningMaterialRequestDto materialRequest;
    private LearningMaterialResponseDto materialResponse;

    @BeforeEach
    void setUp() {
        materialRequest = LearningMaterialRequestDto.builder()
                .title("Understanding Heart Health")
                .author("Dr. John Smith")
                .duration("15 mins")
                .description("A comprehensive guide to heart health")
                .type("VIDEO")
                .videoUrl("https://example.com/video.mp4")
                .category("Cardiology")
                .build();

        materialResponse = LearningMaterialResponseDto.builder()
                .id(1L)
                .title("Understanding Heart Health")
                .author("Dr. John Smith")
                .duration("15 mins")
                .description("A comprehensive guide to heart health")
                .type("VIDEO")
                .videoUrl("https://example.com/video.mp4")
                .category("Cardiology")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Create learning material - Success")
    void createLearningMaterial_Success() throws Exception {
        when(learningMaterialService.createLearningMaterial(any(LearningMaterialRequestDto.class)))
                .thenReturn(materialResponse);

        mockMvc.perform(post("/api/learning-materials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materialRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Understanding Heart Health"));

        verify(learningMaterialService, times(1)).createLearningMaterial(any(LearningMaterialRequestDto.class));
    }

    @Test
    @DisplayName("Create learning material - Validation error for blank title")
    void createLearningMaterial_ValidationError_BlankTitle() throws Exception {
        materialRequest.setTitle("");

        mockMvc.perform(post("/api/learning-materials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materialRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get learning material by ID - Success")
    void getLearningMaterialById_Success() throws Exception {
        when(learningMaterialService.getLearningMaterialById(1L)).thenReturn(materialResponse);

        mockMvc.perform(get("/api/learning-materials/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(learningMaterialService, times(1)).getLearningMaterialById(1L);
    }

    @Test
    @DisplayName("Get all learning materials - Success")
    void getAllLearningMaterials_Success() throws Exception {
        List<LearningMaterialResponseDto> materials = Arrays.asList(materialResponse);
        PaginationResponse<LearningMaterialResponseDto> paginationResponse = PaginationResponse.<LearningMaterialResponseDto>builder()
                .content(materials)
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(learningMaterialService.getAllLearningMaterials(eq(0), eq(10))).thenReturn(paginationResponse);

        mockMvc.perform(get("/api/learning-materials")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray());

        verify(learningMaterialService, times(1)).getAllLearningMaterials(eq(0), eq(10));
    }

    @Test
    @DisplayName("Get materials by category - Success")
    void getMaterialsByCategory_Success() throws Exception {
        List<LearningMaterialResponseDto> materials = Arrays.asList(materialResponse);
        PaginationResponse<LearningMaterialResponseDto> paginationResponse = PaginationResponse.<LearningMaterialResponseDto>builder()
                .content(materials)
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(learningMaterialService.getLearningMaterialsByCategory(eq("Cardiology"), eq(0), eq(10)))
                .thenReturn(paginationResponse);

        mockMvc.perform(get("/api/learning-materials/category/Cardiology")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray());

        verify(learningMaterialService, times(1)).getLearningMaterialsByCategory(eq("Cardiology"), eq(0), eq(10));
    }

    @Test
    @DisplayName("Search materials - Success")
    void searchMaterials_Success() throws Exception {
        List<LearningMaterialResponseDto> materials = Arrays.asList(materialResponse);
        PaginationResponse<LearningMaterialResponseDto> paginationResponse = PaginationResponse.<LearningMaterialResponseDto>builder()
                .content(materials)
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(learningMaterialService.searchLearningMaterials(eq("Heart"), eq(0), eq(10)))
                .thenReturn(paginationResponse);

        mockMvc.perform(get("/api/learning-materials/search")
                        .param("query", "Heart")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(learningMaterialService, times(1)).searchLearningMaterials(eq("Heart"), eq(0), eq(10));
    }

    @Test
    @DisplayName("Update learning material - Success")
    void updateLearningMaterial_Success() throws Exception {
        when(learningMaterialService.updateLearningMaterial(eq(1L), any(LearningMaterialRequestDto.class)))
                .thenReturn(materialResponse);

        mockMvc.perform(put("/api/learning-materials/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materialRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(learningMaterialService, times(1)).updateLearningMaterial(eq(1L), any(LearningMaterialRequestDto.class));
    }

    @Test
    @DisplayName("Delete learning material - Success")
    void deleteLearningMaterial_Success() throws Exception {
        doNothing().when(learningMaterialService).deleteLearningMaterial(1L);

        mockMvc.perform(delete("/api/learning-materials/1"))
                .andExpect(status().isNoContent());

        verify(learningMaterialService, times(1)).deleteLearningMaterial(1L);
    }
}
