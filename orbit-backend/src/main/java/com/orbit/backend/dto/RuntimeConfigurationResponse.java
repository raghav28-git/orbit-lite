package com.orbit.backend.dto;

import java.util.Map;

public class RuntimeConfigurationResponse {

    private final Long environmentId;
    private final Map<String, String> configurations;

    public RuntimeConfigurationResponse(
            Long environmentId,
            Map<String, String> configurations
    ) {
        this.environmentId = environmentId;
        this.configurations = configurations;
    }

    public Long getEnvironmentId() {
        return environmentId;
    }

    public Map<String, String> getConfigurations() {
        return configurations;
    }
}