package com.jendo.app.domain.healthparameter.repository;

import com.jendo.app.domain.healthparameter.entity.HealthParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HealthParameterRepository extends JpaRepository<HealthParameter, Long> {
    
    Page<HealthParameter> findByUserId(Long userId, Pageable pageable);
    
    Optional<HealthParameter> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
