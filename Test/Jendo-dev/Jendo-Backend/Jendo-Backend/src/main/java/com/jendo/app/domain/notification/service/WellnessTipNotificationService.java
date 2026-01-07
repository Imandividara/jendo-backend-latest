package com.jendo.app.domain.notification.service;

import com.jendo.app.domain.notification.entity.ScheduledNotification;
import com.jendo.app.domain.notification.repository.ScheduledNotificationRepository;
import com.jendo.app.domain.wellnessrecommendation.dto.WellnessRecommendationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class WellnessTipNotificationService {

    private final ScheduledNotificationRepository scheduledNotificationRepository;
    
    private static final ZoneId SRI_LANKA_ZONE = ZoneId.of("Asia/Colombo");
    private static final LocalTime START_TIME = LocalTime.of(8, 0); // 8 AM
    private static final LocalTime END_TIME = LocalTime.of(19, 0);  // 7 PM
    private static final int TIPS_PER_DAY = 4;

    @Transactional
    public void scheduleWellnessTipsForUser(Long userId, Map<String, List<WellnessRecommendationDto>> tipsByCategory) {
        try {
            // Delete old unsent wellness tips for this user
            scheduledNotificationRepository.deleteByUserIdAndTypeAndSentFalse(userId, "WELLNESS_TIP");
            log.debug("Deleted old unsent wellness tips for user {}", userId);
            
            // Collect all tips from all categories
            List<WellnessRecommendationDto> allTips = new ArrayList<>();
            tipsByCategory.values().forEach(allTips::addAll);
            
            if (allTips.isEmpty()) {
                log.warn("No wellness tips available to schedule for user {}", userId);
                return;
            }
            
            // Randomly select tips (shuffle and limit to TIPS_PER_DAY)
            Collections.shuffle(allTips);
            List<WellnessRecommendationDto> selectedTips = allTips.stream()
                    .limit(TIPS_PER_DAY)
                    .toList();
            
            // Calculate scheduled times between 8 AM - 7 PM Sri Lanka time
            LocalDateTime now = LocalDateTime.now(SRI_LANKA_ZONE);
            LocalDate today = now.toLocalDate();
            List<LocalDateTime> scheduledTimes = calculateScheduledTimes(today, selectedTips.size(), now);
            
            // Guard against empty time slots (if called after 7 PM)
            if (scheduledTimes.isEmpty()) {
                log.debug("No available time slots for wellness tips today for user {} (after 7 PM)", userId);
                return;
            }
            
            // Create and save scheduled notifications for each tip
            for (int i = 0; i < selectedTips.size() && i < scheduledTimes.size(); i++) {
                WellnessRecommendationDto tip = selectedTips.get(i);
                LocalDateTime scheduledTime = scheduledTimes.get(i);
                
                ScheduledNotification notification = ScheduledNotification.builder()
                        .userId(userId)
                        .type("WELLNESS_TIP")
                        .title(tip.getTitle())
                        .message(tip.getDescription())
                        .scheduledFor(scheduledTime)
                        .sent(false)
                        .category(tip.getCategory())
                        .tipContent(tip.getTitle() + ": " + tip.getDescription())
                        .build();
                
                scheduledNotificationRepository.save(notification);
                log.info("Scheduled wellness tip notification for user {} at {} - Title: {}", 
                        userId, scheduledTime, tip.getTitle());
            }
            
            log.info("Successfully scheduled {} wellness tip notifications for user {}", 
                    Math.min(selectedTips.size(), scheduledTimes.size()), userId);
        } catch (Exception e) {
            log.error("Error scheduling wellness tips for user {}", userId, e);
        }
    }
    
    private List<LocalDateTime> calculateScheduledTimes(LocalDate date, int count, LocalDateTime now) {
        List<LocalDateTime> times = new ArrayList<>();
        
        // Calculate interval in minutes
        long totalMinutes = START_TIME.until(END_TIME, java.time.temporal.ChronoUnit.MINUTES);
        long intervalMinutes = totalMinutes / (count + 1);
        
        for (int i = 1; i <= count; i++) {
            LocalTime time = START_TIME.plusMinutes(intervalMinutes * i);
            LocalDateTime scheduledTime = LocalDateTime.of(date, time);
            
            // If the scheduled time is in the past, skip to avoid immediate sending
            if (scheduledTime.isAfter(now)) {
                times.add(scheduledTime);
            }
        }
        
        return times;
    }
}
