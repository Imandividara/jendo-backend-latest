package com.jendo.app.domain.doctor.repository;

import com.jendo.app.domain.doctor.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    Page<Doctor> findBySpecialtyContainingIgnoreCase(String specialty, Pageable pageable);
    
    List<Doctor> findByIsAvailableTrue();
    
    Optional<Doctor> findByEmail(String email);
    
    Page<Doctor> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
