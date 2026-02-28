package com.example.lab3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.lab3.dto.ExtractRequest;
import com.example.lab3.dto.ExtractResponse;
import com.example.lab3.service.DataProcessingService;

@RestController
@RequestMapping("/api/data")
public class DataExtractorController {

    private final DataProcessingService processingService;

    @Autowired
    public DataExtractorController(DataProcessingService processingService) {
        this.processingService = processingService;
    }

    @PostMapping("/extract")
    public ResponseEntity<ExtractResponse> extract(@RequestBody ExtractRequest request) {
        try {
            String result = processingService.process(
                    request.getType(),
                    request.getData(),
                    request.getPath()
            );
            return ResponseEntity.ok(new ExtractResponse(result));
        } catch (Exception e) {
            ExtractResponse errorResponse = new ExtractResponse();
            errorResponse.setError(e.getMessage());
            return ResponseEntity.ok(errorResponse);
        }
    }
}