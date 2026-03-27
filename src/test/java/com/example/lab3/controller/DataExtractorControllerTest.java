package com.example.lab3.controller;

import com.example.lab3.dto.ExtractRequest;
import com.example.lab3.service.DataProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DataExtractorController.class)
class DataExtractorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DataProcessingService dataProcessingService;

    @Test
    void testExtractSuccess() throws Exception {
        ExtractRequest request = new ExtractRequest("json", "{\"user\":{\"name\":\"Alex\"}}", "user/name");
        when(dataProcessingService.process("json", request.getData(), request.getPath()))
                .thenReturn("Alex");

        mockMvc.perform(post("/api/data/extract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value("Alex"));
    }

    @Test
    void testExtractError() throws Exception {
        ExtractRequest request = new ExtractRequest("json", "{\"user\":{\"name\":\"Alex\"}}", "user/age");
        when(dataProcessingService.process("json", request.getData(), request.getPath()))
                .thenThrow(new IllegalArgumentException("Путь не найден"));

        mockMvc.perform(post("/api/data/extract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Путь не найден"));
    }
}