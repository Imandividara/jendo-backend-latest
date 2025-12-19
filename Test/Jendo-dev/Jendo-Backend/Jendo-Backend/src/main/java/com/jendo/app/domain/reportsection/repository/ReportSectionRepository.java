package com.jendo.app.domain.reportsection.repository;

import com.jendo.app.domain.reportsection.entity.ReportSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportSectionRepository extends JpaRepository<ReportSection, Long> {
    
    Optional<ReportSection> findByName(String name);

    @Query("SELECT s FROM ReportSection s WHERE s.reportCategory.id = :categoryId")
    List<ReportSection> findByCategoryId(@Param("categoryId") Long categoryId);
}
