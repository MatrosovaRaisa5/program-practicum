package com.example.lab3.dto;

public class ExtractResponse {
    private String value;
    private String error;

    public ExtractResponse() {}

    public ExtractResponse(String value) {
        this.value = value;
    }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}

