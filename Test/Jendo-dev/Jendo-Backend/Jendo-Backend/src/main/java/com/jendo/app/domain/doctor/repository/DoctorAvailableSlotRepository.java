package com.jendo.app.domain.doctor.repository;

import com.jendo.app.domain.doctor.entity.DoctorAvailableSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DoctorAvailableSlotRepository extends JpaRepository<DoctorAvailableSlot, Long> {

    List<DoctorAvailableSlot> findByDoctorIdAndSlotDateAndIsBookedFalse(Long doctorId, LocalDate slotDate);

    List<DoctorAvailableSlot> findByDoctorIdAndSlotDateBetweenAndIsBookedFalse(Long doctorId, LocalDate startDate, LocalDate endDate);

    List<DoctorAvailableSlot> findByDoctorIdAndIsBookedFalse(Long doctorId);

    @Query("SELECT DISTINCT s.slotDate FROM DoctorAvailableSlot s WHERE s.doctor.id = :doctorId AND s.isBooked = false AND s.slotDate >= :fromDate ORDER BY s.slotDate")
    List<LocalDate> findAvailableDatesByDoctorId(@Param("doctorId") Long doctorId, @Param("fromDate") LocalDate fromDate);
}
