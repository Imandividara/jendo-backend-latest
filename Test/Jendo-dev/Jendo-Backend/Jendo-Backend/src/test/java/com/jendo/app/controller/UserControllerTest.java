package com.jendo.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.domain.user.dto.UserRequestDto;
import com.jendo.app.domain.user.dto.UserResponseDto;
import com.jendo.app.domain.user.dto.UserUpdateDto;
import com.jendo.app.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserRequestDto userRequest;
    private UserResponseDto userResponse;

    @BeforeEach
    void setUp() {
        userRequest = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .phone("+1234567890")
                .dateOfBirth(LocalDate.of(1990, 1, 15))
                .gender("Male")
                .nationality("American")
                .address("123 Main St")
                .roles(Arrays.asList("USER"))
                .build();

        userResponse = UserResponseDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .dateOfBirth(LocalDate.of(1990, 1, 15))
                .gender("Male")
                .nationality("American")
                .address("123 Main St")
                .roles(Arrays.asList("USER"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Create user - Success")
    void createUser_Success() throws Exception {
        when(userService.createUser(any(UserRequestDto.class))).thenReturn(userResponse);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.firstName").value("John"))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));

        verify(userService, times(1)).createUser(any(UserRequestDto.class));
    }

    @Test
    @DisplayName("Create user - Validation error for blank first name")
    void createUser_ValidationError_BlankFirstName() throws Exception {
        userRequest.setFirstName("");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create user - Validation error for invalid email")
    void createUser_ValidationError_InvalidEmail() throws Exception {
        userRequest.setEmail("invalid-email");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get user by ID - Success")
    void getUserById_Success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userResponse);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.firstName").value("John"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("Get all users - Success")
    void getAllUsers_Success() throws Exception {
        PaginationResponse<UserResponseDto> paginationResponse = PaginationResponse.<UserResponseDto>builder()
                .content(List.of(userResponse))
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(userService.getAllUsers(0, 10)).thenReturn(paginationResponse);

        mockMvc.perform(get("/api/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.totalElements").value(1));

        verify(userService, times(1)).getAllUsers(0, 10);
    }

    @Test
    @DisplayName("Update user - Success")
    void updateUser_Success() throws Exception {
        UserUpdateDto updateDto = UserUpdateDto.builder()
                .firstName("Jane")
                .lastName("Doe")
                .build();

        UserResponseDto updatedResponse = UserResponseDto.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        when(userService.updateUser(eq(1L), any(UserUpdateDto.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.firstName").value("Jane"));

        verify(userService, times(1)).updateUser(eq(1L), any(UserUpdateDto.class));
    }

    @Test
    @DisplayName("Delete user - Success")
    void deleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    @DisplayName("Search users - Success")
    void searchUsers_Success() throws Exception {
        PaginationResponse<UserResponseDto> paginationResponse = PaginationResponse.<UserResponseDto>builder()
                .content(List.of(userResponse))
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(userService.searchUsers("John", 0, 10)).thenReturn(paginationResponse);

        mockMvc.perform(get("/api/users/search")
                        .param("query", "John")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].firstName").value("John"));

        verify(userService, times(1)).searchUsers("John", 0, 10);
    }
}
