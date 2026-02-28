package com.example.lab3.service.extractor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component("json")
public class JsonDataExtractor implements DataExtractor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String extract(String data, String path) throws Exception {
        JsonNode root = objectMapper.readTree(data);
        String jsonPath = path.startsWith("/") ? path : "/" + path;
        JsonNode node = root.at(jsonPath);
        if (node.isMissingNode()) {
            throw new IllegalArgumentException("Путь не найден: " + path);
        }
        return node.asText();
    }
}
