package com.example.lab3.service;

public interface DataProcessingService {
    String process(String type, String data, String path) throws Exception;
}