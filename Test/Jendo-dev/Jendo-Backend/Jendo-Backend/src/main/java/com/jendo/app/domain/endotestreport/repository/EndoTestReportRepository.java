package com.jendo.app.domain.endotestreport.repository;

import com.jendo.app.domain.endotestreport.entity.EndoTestReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EndoTestReportRepository extends JpaRepository<EndoTestReport, Long> {
    
    Page<EndoTestReport> findByUserId(Long userId, Pageable pageable);
    
    List<EndoTestReport> findByJendoTestId(Long jendoTestId);
}
