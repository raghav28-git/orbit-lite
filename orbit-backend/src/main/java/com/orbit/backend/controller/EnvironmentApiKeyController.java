package com.orbit.backend.controller;

import com.orbit.backend.service.EnvironmentApiKeyService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(
        "/api/v1/projects/{projectId}/environments/{environmentId}/api-keys"
)
public class EnvironmentApiKeyController {

    private final EnvironmentApiKeyService apiKeyService;

    public EnvironmentApiKeyController(
            EnvironmentApiKeyService apiKeyService
    ) {
        this.apiKeyService = apiKeyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> generateApiKey(
            @PathVariable Long projectId,
            @PathVariable Long environmentId,
            Authentication authentication
    ) {
        String apiKey = apiKeyService.generateApiKey(
                projectId,
                environmentId,
                authentication.getName()
        );

        return Map.of(
                "apiKey", apiKey
        );
    }

    @DeleteMapping("/{apiKeyId}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void revokeApiKey(
        @PathVariable Long projectId,
        @PathVariable Long environmentId,
        @PathVariable Long apiKeyId,
        Authentication authentication
) {
    apiKeyService.revokeApiKey(
            projectId,
            environmentId,
            apiKeyId,
            authentication.getName()
    );
}
}