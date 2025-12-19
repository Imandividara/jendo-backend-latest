package com.jendo.app.domain.reportcategory.repository;

import com.jendo.app.domain.reportcategory.entity.ReportCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportCategoryRepository extends JpaRepository<ReportCategory, Long> {
    
    Optional<ReportCategory> findByName(String name);
}
