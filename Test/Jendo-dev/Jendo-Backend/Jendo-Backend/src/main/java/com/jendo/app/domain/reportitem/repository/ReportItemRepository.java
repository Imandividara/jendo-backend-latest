package com.jendo.app.domain.reportitem.repository;

import com.jendo.app.domain.reportitem.entity.ReportItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportItemRepository extends JpaRepository<ReportItem, Long> {
    
    List<ReportItem> findByReportSectionId(Long reportSectionId);
    
    @Query("SELECT i FROM ReportItem i WHERE i.reportSection.id = :sectionId")
    List<ReportItem> findBySectionId(@Param("sectionId") Long sectionId);
    
    Page<ReportItem> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
