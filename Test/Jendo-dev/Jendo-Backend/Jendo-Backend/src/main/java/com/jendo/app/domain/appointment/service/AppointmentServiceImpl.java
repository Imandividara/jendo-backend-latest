package com.jendo.app.domain.appointment.service;

import com.jendo.app.common.dto.PaginationResponse;
import com.jendo.app.common.exceptions.NotFoundException;
import com.jendo.app.domain.appointment.dto.AppointmentRequestDto;
import com.jendo.app.domain.appointment.dto.AppointmentResponseDto;
import com.jendo.app.domain.appointment.entity.Appointment;
import com.jendo.app.domain.appointment.mapper.AppointmentMapper;
import com.jendo.app.domain.appointment.repository.AppointmentRepository;
import com.jendo.app.domain.doctor.entity.Doctor;
import com.jendo.app.domain.doctor.repository.DoctorRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);
    
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public AppointmentResponseDto createAppointment(AppointmentRequestDto request) {
        logger.info("Creating new appointment for user ID: {}", request.getUserId());
        
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User", request.getUserId()));
        
        Doctor doctor = null;
        if (request.getDoctorId() != null) {
            doctor = doctorRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new NotFoundException("Doctor", request.getDoctorId()));
        }
        
        Appointment appointment = appointmentMapper.toEntity(request, user, doctor);
        appointment = appointmentRepository.save(appointment);
        
        logger.info("Appointment created successfully with ID: {}", appointment.getId());
        return appointmentMapper.toResponseDto(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponseDto getAppointmentById(Long id) {
        logger.info("Fetching appointment with ID: {}", id);
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment", id));
        return appointmentMapper.toResponseDto(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<AppointmentResponseDto> getAllAppointments(int page, int size) {
        logger.info("Fetching all appointments - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Appointment> appointmentPage = appointmentRepository.findAll(pageable);
        return buildPaginationResponse(appointmentPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<AppointmentResponseDto> getAppointmentsByUserId(Long userId, int page, int size) {
        logger.info("Fetching appointments for user ID: {} - page: {}, size: {}", userId, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Appointment> appointmentPage = appointmentRepository.findByUserId(userId, pageable);
        return buildPaginationResponse(appointmentPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<AppointmentResponseDto> getAppointmentsByDoctorId(Long doctorId, int page, int size) {
        logger.info("Fetching appointments for doctor ID: {} - page: {}, size: {}", doctorId, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Appointment> appointmentPage = appointmentRepository.findByDoctorId(doctorId, pageable);
        return buildPaginationResponse(appointmentPage);
    }

    @Override
    public AppointmentResponseDto updateAppointment(Long id, AppointmentRequestDto request) {
        logger.info("Updating appointment with ID: {}", id);
        
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment", id));
        
        if (request.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new NotFoundException("Doctor", request.getDoctorId()));
            appointment.setDoctor(doctor);
            appointment.setDoctorName(doctor.getName());
        }
        
        if (request.getEmail() != null) appointment.setEmail(request.getEmail());
        if (request.getDate() != null) appointment.setDate(request.getDate());
        if (request.getTime() != null) appointment.setTime(request.getTime());
        if (request.getSpecialty() != null) appointment.setSpecialty(request.getSpecialty());
        if (request.getQualifications() != null) appointment.setQualifications(request.getQualifications());
        if (request.getType() != null) appointment.setType(request.getType());
        if (request.getStatus() != null) appointment.setStatus(request.getStatus());
        
        appointment = appointmentRepository.save(appointment);
        logger.info("Appointment updated successfully with ID: {}", id);
        return appointmentMapper.toResponseDto(appointment);
    }

    @Override
    public AppointmentResponseDto updateAppointmentStatus(Long id, String status) {
        logger.info("Updating appointment status for ID: {} to {}", id, status);
        
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment", id));
        
        appointment.setStatus(status);
        appointment = appointmentRepository.save(appointment);
        
        logger.info("Appointment status updated successfully for ID: {}", id);
        return appointmentMapper.toResponseDto(appointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        logger.info("Deleting appointment with ID: {}", id);
        
        if (!appointmentRepository.existsById(id)) {
            throw new NotFoundException("Appointment", id);
        }
        
        appointmentRepository.deleteById(id);
        logger.info("Appointment deleted successfully with ID: {}", id);
    }
    
    private PaginationResponse<AppointmentResponseDto> buildPaginationResponse(Page<Appointment> appointmentPage) {
        List<AppointmentResponseDto> content = appointmentPage.getContent().stream()
                .map(appointmentMapper::toResponseDto)
                .collect(Collectors.toList());
        
        return PaginationResponse.<AppointmentResponseDto>builder()
                .content(content)
                .pageNumber(appointmentPage.getNumber())
                .pageSize(appointmentPage.getSize())
                .totalElements(appointmentPage.getTotalElements())
                .totalPages(appointmentPage.getTotalPages())
                .first(appointmentPage.isFirst())
                .last(appointmentPage.isLast())
                .build();
    }
}
