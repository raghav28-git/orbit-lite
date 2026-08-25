package com.orbit.backend.dto;

import com.orbit.backend.entity.Configuration;

import java.time.LocalDateTime;

public class ConfigurationResponse {

    private Long id;
    private String key;
    private String value;
    private Long environmentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ConfigurationResponse(
            Long id,
            String key,
            String value,
            Long environmentId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.environmentId = environmentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ConfigurationResponse from(
            Configuration configuration
    ) {
        return new ConfigurationResponse(
                configuration.getId(),
                configuration.getKey(),
                configuration.getValue(),
                configuration.getEnvironment().getId(),
                configuration.getCreatedAt(),
                configuration.getUpdatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Long getEnvironmentId() {
        return environmentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}