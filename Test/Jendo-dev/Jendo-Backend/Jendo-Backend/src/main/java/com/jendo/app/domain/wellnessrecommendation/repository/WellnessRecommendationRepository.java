package com.jendo.app.domain.wellnessrecommendation.repository;

import com.jendo.app.domain.wellnessrecommendation.entity.WellnessRecommendation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WellnessRecommendationRepository extends JpaRepository<WellnessRecommendation, Long> {
    
    List<WellnessRecommendation> findByRiskLevelIgnoreCaseAndIsActiveTrue(String riskLevel);
    
    List<WellnessRecommendation> findByRiskLevelIgnoreCaseAndIsActiveTrueOrderByPriorityAsc(String riskLevel);
    
    Page<WellnessRecommendation> findByCategory(String category, Pageable pageable);
    
    Page<WellnessRecommendation> findByRiskLevel(String riskLevel, Pageable pageable);
    
    Page<WellnessRecommendation> findByIsActiveTrue(Pageable pageable);
}
