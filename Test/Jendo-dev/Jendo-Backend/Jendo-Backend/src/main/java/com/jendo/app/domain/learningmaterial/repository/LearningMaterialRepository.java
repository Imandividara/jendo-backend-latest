package com.jendo.app.domain.learningmaterial.repository;

import com.jendo.app.domain.learningmaterial.entity.LearningMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningMaterialRepository extends JpaRepository<LearningMaterial, Long> {
    
    Page<LearningMaterial> findByCategory(String category, Pageable pageable);
    
    Page<LearningMaterial> findByType(String type, Pageable pageable);
    
    Page<LearningMaterial> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
