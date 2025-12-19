package com.jendo.app.domain.appointment.repository;

import com.jendo.app.domain.appointment.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    Page<Appointment> findByUserId(Long userId, Pageable pageable);
    
    Page<Appointment> findByDoctorId(Long doctorId, Pageable pageable);
    
    List<Appointment> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    
    List<Appointment> findByStatus(String status);
    
    List<Appointment> findByDoctorIdAndDate(Long doctorId, LocalDate date);
}
