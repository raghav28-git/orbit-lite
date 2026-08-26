package com.orbit.backend.dto;

import com.orbit.backend.entity.EnvironmentApiKey;

import java.time.LocalDateTime;

public record EnvironmentApiKeyResponse(
        Long id,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime revokedAt
) {

    public static EnvironmentApiKeyResponse from(
            EnvironmentApiKey apiKey
    ) {
        return new EnvironmentApiKeyResponse(
                apiKey.getId(),
                apiKey.isActive(),
                apiKey.getCreatedAt(),
                apiKey.getRevokedAt()
        );
    }
}