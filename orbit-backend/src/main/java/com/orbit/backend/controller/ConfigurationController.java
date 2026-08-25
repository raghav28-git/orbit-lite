package com.orbit.backend.controller;

import com.orbit.backend.dto.ConfigurationResponse;
import com.orbit.backend.dto.CreateConfigurationRequest;
import com.orbit.backend.entity.Configuration;
import com.orbit.backend.service.ConfigurationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.orbit.backend.dto.UpdateConfigurationRequest;
import java.util.List;
import com.orbit.backend.dto.RuntimeConfigurationResponse;
import com.orbit.backend.dto.ConfigurationVersionResponse;
import java.util.List;

@RestController
@RequestMapping(
        "/api/v1/projects/{projectId}/environments/{environmentId}/configurations"
)
public class ConfigurationController {

    private final ConfigurationService configurationService;

    public ConfigurationController(
            ConfigurationService configurationService
    ) {
        this.configurationService = configurationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConfigurationResponse createConfiguration(
            @PathVariable Long projectId,
            @PathVariable Long environmentId,
            @RequestBody CreateConfigurationRequest request,
            Authentication authentication
    ) {
        Configuration configuration =
                configurationService.createConfiguration(
                        projectId,
                        environmentId,
                        request,
                        authentication.getName()
                );

        return ConfigurationResponse.from(configuration);
    }

    @GetMapping
    public List<ConfigurationResponse> getConfigurations(
            @PathVariable Long projectId,
            @PathVariable Long environmentId,
            Authentication authentication
    ) {
        return configurationService
                .getConfigurations(
                        projectId,
                        environmentId,
                        authentication.getName()
                )
                .stream()
                .map(ConfigurationResponse::from)
                .toList();
    }
        @PutMapping("/{configurationId}")
        public ConfigurationResponse updateConfiguration(
                @PathVariable Long projectId,
                @PathVariable Long environmentId,
                @PathVariable Long configurationId,
                @RequestBody UpdateConfigurationRequest request,
                Authentication authentication
        ) {
        Configuration configuration =
                configurationService.updateConfiguration(
                        projectId,
                        environmentId,
                        configurationId,
                        request,
                        authentication.getName()
                );

        return ConfigurationResponse.from(configuration);
        }
        @GetMapping("/runtime")
        public RuntimeConfigurationResponse getRuntimeConfiguration(
                @PathVariable Long projectId,
                @PathVariable Long environmentId,
                Authentication authentication
        ) {
        return configurationService.getRuntimeConfiguration(
                projectId,
                environmentId,
                authentication.getName()
        );
        }

        @GetMapping("/{configurationId}/versions")
public List<ConfigurationVersionResponse> getConfigurationVersions(
        @PathVariable Long projectId,
        @PathVariable Long environmentId,
        @PathVariable Long configurationId,
        Authentication authentication
) {
    return configurationService.getConfigurationVersions(
            projectId,
            environmentId,
            configurationId,
            authentication.getName()
    );
}

        
}