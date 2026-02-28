package com.example.lab3.service.extractor;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component("xml")
public class XmlDataExtractor implements DataExtractor {

    private final XmlMapper xmlMapper = new XmlMapper();

    @Override
    public String extract(String data, String path) throws Exception {
        JsonNode root = xmlMapper.readTree(data);
        String jsonPath = path.startsWith("/") ? path : "/" + path;
        JsonNode node = root.at(jsonPath);
        if (node.isMissingNode()) {
            throw new IllegalArgumentException("Путь не найден: " + path);
        }
        return node.asText();
    }
}
