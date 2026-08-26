package com.orbit.backend.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateConfigurationRequest {

    @NotBlank
    @Size(max = 100)
    private String key;

    @NotBlank
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