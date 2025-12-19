package com.jendo.app.domain.consultationfee.repository;

import com.jendo.app.domain.consultationfee.entity.ConsultationFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationFeeRepository extends JpaRepository<ConsultationFee, Long> {
    
    List<ConsultationFee> findByDoctorId(Long doctorId);
    
    void deleteByDoctorId(Long doctorId);
}
