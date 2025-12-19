package com.jendo.app.domain.appointment.mapper;

import com.jendo.app.domain.appointment.dto.AppointmentRequestDto;
import com.jendo.app.domain.appointment.dto.AppointmentResponseDto;
import com.jendo.app.domain.appointment.entity.Appointment;
import com.jendo.app.domain.doctor.entity.Doctor;
import com.jendo.app.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public Appointment toEntity(AppointmentRequestDto dto, User user, Doctor doctor) {
        return Appointment.builder()
                .user(user)
                .doctor(doctor)
                .email(dto.getEmail())
                .date(dto.getDate())
                .time(dto.getTime())
                .specialty(dto.getSpecialty())
                .qualifications(dto.getQualifications())
                .doctorName(dto.getDoctorName() != null ? dto.getDoctorName() : 
                        (doctor != null ? doctor.getName() : null))
                .type(dto.getType())
                .status(dto.getStatus() != null ? dto.getStatus() : "SCHEDULED")
                .build();
    }

    public AppointmentResponseDto toResponseDto(Appointment entity) {
        return AppointmentResponseDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .userName(entity.getUser().getFirstName() + " " + entity.getUser().getLastName())
                .doctorId(entity.getDoctor() != null ? entity.getDoctor().getId() : null)
                .doctorName(entity.getDoctorName())
                .email(entity.getEmail())
                .date(entity.getDate())
                .time(entity.getTime())
                .specialty(entity.getSpecialty())
                .qualifications(entity.getQualifications())
                .type(entity.getType())
                .status(entity.getStatus())
                .build();
    }
}
