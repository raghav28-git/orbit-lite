package com.orbit.backend.dto;
import jakarta.validation.constraints.NotBlank;

public class UpdateConfigurationRequest {

    @NotBlank
    private String value;

    public UpdateConfigurationRequest() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}