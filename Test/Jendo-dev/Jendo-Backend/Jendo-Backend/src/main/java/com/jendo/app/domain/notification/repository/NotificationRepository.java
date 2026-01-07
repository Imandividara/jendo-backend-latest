package com.jendo.app.domain.notification.repository;

import com.jendo.app.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    Page<Notification> findByUserIdAndTypeNotInOrderByCreatedAtDesc(Long userId, List<String> excludedTypes, Pageable pageable);
    
    List<Notification> findByUserIdAndIsReadFalseAndTypeNotIn(Long userId, List<String> excludedTypes);
    
    long countByUserIdAndIsReadFalseAndTypeNotIn(Long userId, List<String> excludedTypes);
}
