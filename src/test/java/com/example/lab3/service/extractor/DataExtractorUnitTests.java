package com.example.lab3.service.extractor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataExtractorUnitTests {

    private JsonDataExtractor jsonExtractor;
    private XmlDataExtractor xmlExtractor;
    private YamlDataExtractor yamlExtractor;

    @BeforeEach
    void setUp() {
        jsonExtractor = new JsonDataExtractor();
        xmlExtractor = new XmlDataExtractor();
        yamlExtractor = new YamlDataExtractor();
    }

    @Test
    void testJsonExtractor_Success() throws Exception {
        String json = "{ \"user\": { \"name\": \"Alex\" } }";
        String result = jsonExtractor.extract(json, "user/name");
        assertEquals("Alex", result);
    }

    @Test
    void testJsonExtractor_PathNotFound() {
        String json = "{ \"user\": { \"name\": \"Alex\" } }";
        assertThrows(IllegalArgumentException.class, () ->
                jsonExtractor.extract(json, "user/age"));
    }

    @Test
    void testXmlExtractor_Success() throws Exception {
        String xml = "<root><user><name>Alex</name></user></root>";
        String result = xmlExtractor.extract(xml, "/user/name");
        assertEquals("Alex", result);
    }

    @Test
    void testXmlExtractor_PathNotFound() {
        String xml = "<root><user><name>Alex</name></user></root>";
        assertThrows(IllegalArgumentException.class, () ->
                xmlExtractor.extract(xml, "/root/user/age"));
    }

    @Test
    void testYamlExtractor_Success() throws Exception {
        String yaml = "user:\n  name: Alex\n  age: 30";
        String result = yamlExtractor.extract(yaml, "user/name");
        assertEquals("Alex", result);
    }

    @Test
    void testYamlExtractor_ArrayAccess() throws Exception {
        String yaml = "users:\n  - name: Alex\n  - name: Bob";
        String result = yamlExtractor.extract(yaml, "users/0/name");
        assertEquals("Alex", result);
    }

    @Test
    void testYamlExtractor_PathNotFound() {
        String yaml = "user:\n  name: Alex";
        assertThrows(IllegalArgumentException.class, () ->
                yamlExtractor.extract(yaml, "user/age"));
    }
}