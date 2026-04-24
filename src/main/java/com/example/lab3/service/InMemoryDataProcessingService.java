package com.example.lab3.service;

import com.example.lab3.service.extractor.DataExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service("inMemoryCacheService")
public class InMemoryDataProcessingService implements DataProcessingService {

    private static final Logger log = LoggerFactory.getLogger(InMemoryDataProcessingService.class);

    private final Map<String, DataExtractor> extractorMap;
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    @Autowired
    public InMemoryDataProcessingService(Map<String, DataExtractor> extractorMap) {
        this.extractorMap = extractorMap;
    }

    @Override
    public String process(String type, String data, String path) throws Exception {
        String key = type + "::" + data.hashCode() + "::" + path;

        String cached = cache.get(key);
        if (cached != null) {
            log.info("In-memory Cache HIT for key: {}", key);
            return cached;
        }
        log.info("In-memory Cache MISS for key: {}", key);

        DataExtractor extractor = extractorMap.get(type.toLowerCase());
        if (extractor == null) {
            throw new IllegalArgumentException("Неподдерживаемый тип: " + type);
        }

        String result = extractor.extract(data, path);
        cache.put(key, result);
        return result;
    }

    public Map<String, String> getCache() {
        return cache;
    }
}