package com.jendo.app.domain.jendoreport.repository;

import com.jendo.app.domain.jendoreport.entity.JendoReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JendoReportRepository extends JpaRepository<JendoReport, Long> {
    
    Page<JendoReport> findByUserId(Long userId, Pageable pageable);
    
    List<JendoReport> findByUserIdOrderByUploadedAtDesc(Long userId);
}
