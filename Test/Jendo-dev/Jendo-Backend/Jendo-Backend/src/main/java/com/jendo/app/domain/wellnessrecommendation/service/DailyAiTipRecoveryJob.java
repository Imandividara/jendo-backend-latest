package com.jendo.app.domain.wellnessrecommendation.service;

import com.jendo.app.domain.wellnessrecommendation.repository.DailyAiTipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Ensures users have fresh daily AI tips even if the server was down at 06:00.
 * - On startup: purge expired tips and (re)generate missing tips for current window
 * - Periodically (every 30 minutes 06:00-23:59 Asia/Colombo): purge expired and ensure generation
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DailyAiTipRecoveryJob {

    private final DailyAiTipRepository dailyAiTipRepository;
    private final WellnessRecommendationService wellnessRecommendationService;

    @EventListener(ApplicationReadyEvent.class)
    public void ensureTipsOnStartup() {
        LocalDateTime now = LocalDateTime.now();
        try {
            // 1) Cleanup any expired windows left over
            dailyAiTipRepository.deleteExpired(now);
            log.info("[Recovery] Purged expired daily AI tips at startup");
        } catch (Exception ex) {
            log.error("[Recovery] Failed to purge expired daily AI tips at startup", ex);
        }

        try {
            // 2) (Re)generate tips for current window if missing (idempotent)
            wellnessRecommendationService.generateDailyTipsForAllUsers();
            log.info("[Recovery] Ensured daily AI tips exist for current window at startup");
        } catch (Exception ex) {
            log.error("[Recovery] Failed to ensure daily AI tips at startup", ex);
        }
    }

    // Run every 30 minutes between 06:00 - 23:59 Sri Lanka time
    @Scheduled(cron = "0 */30 6-23 * * *", zone = "Asia/Colombo")
    public void ensureTipsPeriodically() {
        LocalDateTime now = LocalDateTime.now();
        try {
            dailyAiTipRepository.deleteExpired(now);
        } catch (Exception ex) {
            log.error("[Recovery] Periodic purge of expired tips failed", ex);
        }

        try {
            wellnessRecommendationService.generateDailyTipsForAllUsers();
            log.debug("[Recovery] Periodic ensure run completed");
        } catch (Exception ex) {
            log.error("[Recovery] Periodic ensure failed", ex);
        }
    }
}
