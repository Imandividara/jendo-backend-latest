package com.jendo.app.domain.doctor.mapper;

import com.jendo.app.domain.consultationfee.entity.ConsultationFee;
import com.jendo.app.domain.doctor.dto.DoctorRequestDto;
import com.jendo.app.domain.doctor.dto.DoctorResponseDto;
import com.jendo.app.domain.doctor.entity.Doctor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class DoctorMapper {

    public Doctor toEntity(DoctorRequestDto dto) {
        return Doctor.builder()
                .name(dto.getName())
                .specialty(dto.getSpecialty())
                .hospital(dto.getHospital())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .qualifications(dto.getQualifications())
                .imageUrl(dto.getImageUrl())
                .address(dto.getAddress())
                .isAvailable(dto.getIsAvailable() != null ? dto.getIsAvailable() : true)
                .availableDays(dto.getAvailableDays())
                .build();
    }

    public DoctorResponseDto toResponseDto(Doctor entity) {
        return DoctorResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .specialty(entity.getSpecialty())
                .hospital(entity.getHospital())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .qualifications(entity.getQualifications())
                .imageUrl(entity.getImageUrl())
                .address(entity.getAddress())
                .isAvailable(entity.getIsAvailable())
                .availableDays(entity.getAvailableDays())
                .consultationFees(entity.getConsultationFees() != null
                        ? entity.getConsultationFees().stream()
                                .map(this::toConsultationFeeDto)
                                .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }
    
    private DoctorResponseDto.ConsultationFeeDto toConsultationFeeDto(ConsultationFee fee) {
        return DoctorResponseDto.ConsultationFeeDto.builder()
                .id(fee.getId())
                .feeType(fee.getFeeType())
                .amount(fee.getAmount())
                .currency(fee.getCurrency())
                .build();
    }
}
