package com.example.lab3.db.repository;

import com.example.lab3.db.entity.CacheEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public interface CacheEntryRepository extends JpaRepository<CacheEntry, Long> {

    Optional<CacheEntry> findByKey(String key);

    @Modifying
    @Transactional
    @Query("DELETE FROM CacheEntry e WHERE e.createdAt < :threshold")
    int deleteOlderThan(@Param("threshold") LocalDateTime threshold);

    @Modifying
    @Transactional
    void deleteAll();
}