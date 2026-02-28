package com.example.lab3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.lab3.service.extractor.DataExtractor;
import java.util.Map;

@Service
public class DataProcessingService {

    private final Map<String, DataExtractor> extractorMap;

    @Autowired
    public DataProcessingService(Map<String, DataExtractor> extractorMap) {
        this.extractorMap = extractorMap;
    }

    public String process(String type, String data, String path) throws Exception {
        DataExtractor extractor = extractorMap.get(type.toLowerCase());
        if (extractor == null) {
            throw new IllegalArgumentException("Неподдерживаемый  тип: " + type);
        }
        return extractor.extract(data, path);
    }
}
