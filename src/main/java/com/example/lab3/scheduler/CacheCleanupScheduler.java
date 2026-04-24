package com.example.lab3.scheduler;

import com.example.lab3.db.repository.CacheEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class CacheCleanupScheduler {

    private static final Logger log = LoggerFactory.getLogger(CacheCleanupScheduler.class);
    private final CacheEntryRepository cacheRepository;

    @Autowired
    public CacheCleanupScheduler(CacheEntryRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    @Scheduled(fixedDelay = 30000, initialDelay = 5000)
    @Transactional
    public void evictOldEntries() {
        try {
            LocalDateTime threshold = LocalDateTime.now().minusSeconds(60);
            int deleted = cacheRepository.deleteOlderThan(threshold);
            if (deleted > 0) {
                log.info("Планировщик: удалено {} старых записей (старше 60 сек)", deleted);
            }
        } catch (Exception e) {
            log.error("Ошибка при удалении старых записей: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Scheduled(cron = "0 0 0 * * SUN")
    @Transactional
    public void clearAllCache() {
        try {
            cacheRepository.deleteAll();
            log.info("Планировщик: выполнена полная очистка кэша (воскресенье)");
        } catch (Exception e) {
            log.error("Ошибка при полной очистке кэша: {}", e.getMessage(), e);
            throw e;
        }
    }
}