package com.orbit.backend.dto;

import com.orbit.backend.entity.Environment;

import java.time.LocalDateTime;

public class EnvironmentResponse {

    private Long id;
    private String name;
    private Long projectId;
    private LocalDateTime createdAt;

    public EnvironmentResponse(
            Long id,
            String name,
            Long projectId,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.name = name;
        this.projectId = projectId;
        this.createdAt = createdAt;
    }

    public static EnvironmentResponse from(Environment environment) {
        return new EnvironmentResponse(
                environment.getId(),
                environment.getName(),
                environment.getProject().getId(),
                environment.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getProjectId() {
        return projectId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}