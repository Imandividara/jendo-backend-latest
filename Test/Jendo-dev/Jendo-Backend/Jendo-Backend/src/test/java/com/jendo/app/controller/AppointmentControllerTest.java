package com.jendo.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.domain.appointment.dto.AppointmentRequestDto;
import com.jendo.app.domain.appointment.dto.AppointmentResponseDto;
import com.jendo.app.domain.appointment.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppointmentService appointmentService;

    private AppointmentRequestDto appointmentRequest;
    private AppointmentResponseDto appointmentResponse;

    @BeforeEach
    void setUp() {
        appointmentRequest = AppointmentRequestDto.builder()
                .userId(1L)
                .doctorId(1L)
                .email("patient@example.com")
                .date(LocalDate.now().plusDays(7))
                .time(LocalTime.of(10, 30))
                .specialty("Cardiology")
                .type("IN_PERSON")
                .status("SCHEDULED")
                .build();

        appointmentResponse = AppointmentResponseDto.builder()
                .id(1L)
                .userId(1L)
                .userName("John Doe")
                .doctorId(1L)
                .doctorName("Dr. Jane Smith")
                .email("patient@example.com")
                .date(LocalDate.now().plusDays(7))
                .time(LocalTime.of(10, 30))
                .specialty("Cardiology")
                .type("IN_PERSON")
                .status("SCHEDULED")
                .build();
    }

    @Test
    @DisplayName("Create appointment - Success")
    void createAppointment_Success() throws Exception {
        when(appointmentService.createAppointment(any(AppointmentRequestDto.class))).thenReturn(appointmentResponse);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("SCHEDULED"));

        verify(appointmentService, times(1)).createAppointment(any(AppointmentRequestDto.class));
    }

    @Test
    @DisplayName("Create appointment - Validation error for null userId")
    void createAppointment_ValidationError_NullUserId() throws Exception {
        appointmentRequest.setUserId(null);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get appointment by ID - Success")
    void getAppointmentById_Success() throws Exception {
        when(appointmentService.getAppointmentById(1L)).thenReturn(appointmentResponse);

        mockMvc.perform(get("/api/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(appointmentService, times(1)).getAppointmentById(1L);
    }

    @Test
    @DisplayName("Get appointments by user ID - Success")
    void getAppointmentsByUserId_Success() throws Exception {
        PaginationResponse<AppointmentResponseDto> paginationResponse = PaginationResponse.<AppointmentResponseDto>builder()
                .content(List.of(appointmentResponse))
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(appointmentService.getAppointmentsByUserId(1L, 0, 10)).thenReturn(paginationResponse);

        mockMvc.perform(get("/api/appointments/user/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(appointmentService, times(1)).getAppointmentsByUserId(1L, 0, 10);
    }

    @Test
    @DisplayName("Update appointment status - Success")
    void updateAppointmentStatus_Success() throws Exception {
        appointmentResponse.setStatus("COMPLETED");
        when(appointmentService.updateAppointmentStatus(1L, "COMPLETED")).thenReturn(appointmentResponse);

        mockMvc.perform(patch("/api/appointments/1/status")
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("COMPLETED"));

        verify(appointmentService, times(1)).updateAppointmentStatus(1L, "COMPLETED");
    }

    @Test
    @DisplayName("Delete appointment - Success")
    void deleteAppointment_Success() throws Exception {
        doNothing().when(appointmentService).deleteAppointment(1L);

        mockMvc.perform(delete("/api/appointments/1"))
                .andExpect(status().isNoContent());

        verify(appointmentService, times(1)).deleteAppointment(1L);
    }
}
