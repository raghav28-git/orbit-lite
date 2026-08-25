package com.orbit.backend.dto;

import java.time.LocalDateTime;

public class ConfigurationVersionResponse {

    private final Integer version;
    private final String value;
    private final LocalDateTime createdAt;

    public ConfigurationVersionResponse(
            Integer version,
            String value,
            LocalDateTime createdAt
    ) {
        this.version = version;
        this.value = value;
        this.createdAt = createdAt;
    }

    public Integer getVersion() {
        return version;
    }

    public String getValue() {
        return value;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}