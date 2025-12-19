package com.jendo.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.domain.doctor.dto.DoctorRequestDto;
import com.jendo.app.domain.doctor.dto.DoctorResponseDto;
import com.jendo.app.domain.doctor.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoctorService doctorService;

    private DoctorRequestDto doctorRequest;
    private DoctorResponseDto doctorResponse;

    @BeforeEach
    void setUp() {
        doctorRequest = DoctorRequestDto.builder()
                .name("Dr. Jane Smith")
                .specialty("Cardiology")
                .hospital("City General Hospital")
                .email("jane.smith@hospital.com")
                .phone("+1234567890")
                .qualifications("MD, PhD, FACC")
                .isAvailable(true)
                .availableDays("Monday, Wednesday, Friday")
                .build();

        doctorResponse = DoctorResponseDto.builder()
                .id(1L)
                .name("Dr. Jane Smith")
                .specialty("Cardiology")
                .hospital("City General Hospital")
                .email("jane.smith@hospital.com")
                .phone("+1234567890")
                .qualifications("MD, PhD, FACC")
                .isAvailable(true)
                .availableDays("Monday, Wednesday, Friday")
                .consultationFees(Collections.emptyList())
                .build();
    }

    @Test
    @DisplayName("Create doctor - Success")
    void createDoctor_Success() throws Exception {
        when(doctorService.createDoctor(any(DoctorRequestDto.class))).thenReturn(doctorResponse);

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Dr. Jane Smith"))
                .andExpect(jsonPath("$.data.specialty").value("Cardiology"));

        verify(doctorService, times(1)).createDoctor(any(DoctorRequestDto.class));
    }

    @Test
    @DisplayName("Create doctor - Validation error for blank name")
    void createDoctor_ValidationError_BlankName() throws Exception {
        doctorRequest.setName("");

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get doctor by ID - Success")
    void getDoctorById_Success() throws Exception {
        when(doctorService.getDoctorById(1L)).thenReturn(doctorResponse);

        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(doctorService, times(1)).getDoctorById(1L);
    }

    @Test
    @DisplayName("Get all doctors - Success")
    void getAllDoctors_Success() throws Exception {
        PaginationResponse<DoctorResponseDto> paginationResponse = PaginationResponse.<DoctorResponseDto>builder()
                .content(List.of(doctorResponse))
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(doctorService.getAllDoctors(0, 10)).thenReturn(paginationResponse);

        mockMvc.perform(get("/api/doctors")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray());

        verify(doctorService, times(1)).getAllDoctors(0, 10);
    }

    @Test
    @DisplayName("Get available doctors - Success")
    void getAvailableDoctors_Success() throws Exception {
        when(doctorService.getAvailableDoctors()).thenReturn(List.of(doctorResponse));

        mockMvc.perform(get("/api/doctors/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());

        verify(doctorService, times(1)).getAvailableDoctors();
    }

    @Test
    @DisplayName("Update doctor - Success")
    void updateDoctor_Success() throws Exception {
        when(doctorService.updateDoctor(eq(1L), any(DoctorRequestDto.class))).thenReturn(doctorResponse);

        mockMvc.perform(put("/api/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(doctorService, times(1)).updateDoctor(eq(1L), any(DoctorRequestDto.class));
    }

    @Test
    @DisplayName("Delete doctor - Success")
    void deleteDoctor_Success() throws Exception {
        doNothing().when(doctorService).deleteDoctor(1L);

        mockMvc.perform(delete("/api/doctors/1"))
                .andExpect(status().isNoContent());

        verify(doctorService, times(1)).deleteDoctor(1L);
    }
}
