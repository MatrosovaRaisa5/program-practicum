package com.example.lab3.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DataProcessingServiceTest {

    @Autowired
    private InMemoryDataProcessingService dataProcessingService;


    @Test
    void testCorrectExtractorIsChosen() throws Exception {
        // для json
        String jsonData = "{ \"user\": { \"name\": \"Alex\" } }";
        String jsonResult = dataProcessingService.process("json", jsonData, "user/name");
        assertEquals("Alex", jsonResult);

        // для xml
        String xmlData = "<root><user><name>Alex</name></user></root>";
        String xmlResult = dataProcessingService.process("xml", xmlData, "/user/name");
        assertEquals("Alex", xmlResult);

        // для yaml
        String yamlData = "user:\n  name: Alex";
        String yamlResult = dataProcessingService.process("yaml", yamlData, "user/name");
        assertEquals("Alex", yamlResult);
    }

    @Test
    void testCachingWorks() throws Exception {
        String type = "json";
        String data = "{ \"user\": { \"name\": \"Alex\" } }";
        String path = "user/name";
        String expected = "Alex";

        dataProcessingService.getCache().clear();

        String result1 = dataProcessingService.process(type, data, path);
        assertEquals(expected, result1);

        String key = type + "::" + data.hashCode() + "::" + path;
        assertTrue(dataProcessingService.getCache().containsKey(key));

        String result2 = dataProcessingService.process(type, data, path);
        assertEquals(expected, result2);

        assertEquals(1, dataProcessingService.getCache().size());
    }

    @Test
    void testDifferentDataDifferentCache() throws Exception {
        String type = "json";
        String data1 = "{ \"user\": { \"name\": \"Alex\" } }";
        String data2 = "{ \"user\": { \"name\": \"Bob\" } }";
        String path = "user/name";

        dataProcessingService.getCache().clear();

        String result1 = dataProcessingService.process(type, data1, path);
        String result2 = dataProcessingService.process(type, data2, path);

        assertEquals("Alex", result1);
        assertEquals("Bob", result2);

        String key1 = type + "::" + data1.hashCode() + "::" + path;
        String key2 = type + "::" + data2.hashCode() + "::" + path;
        assertTrue(dataProcessingService.getCache().containsKey(key1));
        assertTrue(dataProcessingService.getCache().containsKey(key2));
        assertEquals(2, dataProcessingService.getCache().size());
    }
}