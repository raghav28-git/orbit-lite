package com.orbit.backend.dto;

public class CreateConfigurationRequest {

    private String key;
    private String value;

    public CreateConfigurationRequest() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}