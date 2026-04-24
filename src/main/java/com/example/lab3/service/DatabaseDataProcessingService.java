package com.example.lab3.service;

import com.example.lab3.db.entity.CacheEntry;
import com.example.lab3.db.repository.CacheEntryRepository;
import com.example.lab3.service.extractor.DataExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Map;
import java.util.Optional;

@Primary
@Service("databaseCacheService")
public class DatabaseDataProcessingService implements DataProcessingService {

    private static final Logger log = LoggerFactory.getLogger(DatabaseDataProcessingService.class);

    private final Map<String, DataExtractor> extractorMap;
    private final CacheEntryRepository cacheRepository;

    @Autowired
    public DatabaseDataProcessingService(Map<String, DataExtractor> extractorMap,
                                         CacheEntryRepository cacheRepository) {
        this.extractorMap = extractorMap;
        this.cacheRepository = cacheRepository;
    }

    @Override
    @Transactional
    public String process(String type, String data, String path) throws Exception {
        // Генерируем уникальный ключ через SHA-256
        String key = generateKey(type, data, path);

        Optional<CacheEntry> optional = cacheRepository.findByKey(key);
        if (optional.isPresent()) {
            log.info("Database Cache HIT for key: {}", key);
            return optional.get().getValue();
        }
        log.info("Database Cache MISS for key: {}", key);

        DataExtractor extractor = extractorMap.get(type.toLowerCase());
        if (extractor == null) {
            throw new IllegalArgumentException("Неподдерживаемый тип: " + type);
        }

        String result = extractor.extract(data, path);
        CacheEntry entry = new CacheEntry(key, result, LocalDateTime.now());

        try {
            cacheRepository.save(entry);
        } catch (DataIntegrityViolationException e) {
            // Другой поток вставил запись с таким же ключом – получаем актуальное значение
            log.warn("Конфликт сохранения ключа {}, читаем вставленную запись", key);
            return cacheRepository.findByKey(key)
                    .map(CacheEntry::getValue)
                    .orElseThrow(() -> new IllegalStateException("Не удалось прочитать кэш после конфликта"));
        }
        return result;
    }

    private String generateKey(String type, String data, String path) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            String dataHash = HexFormat.of().formatHex(hash);
            return type + "::" + dataHash + "::" + path;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 не доступен", e);
        }
    }
}