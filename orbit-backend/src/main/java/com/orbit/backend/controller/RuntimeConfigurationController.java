package com.orbit.backend.controller;

import com.orbit.backend.entity.Environment;
import com.orbit.backend.entity.EnvironmentApiKey;
import com.orbit.backend.service.ConfigurationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.orbit.backend.dto.RuntimeConfigurationResponse;

@RestController
@RequestMapping("/api/v1/runtime")
public class RuntimeConfigurationController {

    private final ConfigurationService configurationService;

    public RuntimeConfigurationController(
            ConfigurationService configurationService
    ) {
        this.configurationService = configurationService;
    }

    @GetMapping("/configurations")
    public RuntimeConfigurationResponse getRuntimeConfiguration(
            Authentication authentication
    ) {
        EnvironmentApiKey apiKey =
                (EnvironmentApiKey) authentication.getDetails();

        Environment environment = apiKey.getEnvironment();

        return configurationService.getRuntimeConfiguration(
                environment
        );
    }
}