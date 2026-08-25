package com.orbit.backend.controller;

import com.orbit.backend.dto.CreateEnvironmentRequest;
import com.orbit.backend.dto.EnvironmentResponse;
import com.orbit.backend.entity.Environment;
import com.orbit.backend.service.EnvironmentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/environments")
public class EnvironmentController {

    private final EnvironmentService environmentService;

    public EnvironmentController(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnvironmentResponse createEnvironment(
            @PathVariable Long projectId,
            @RequestBody CreateEnvironmentRequest request,
            Authentication authentication
    ) {
        Environment environment =
                environmentService.createEnvironment(
                        projectId,
                        request,
                        authentication.getName()
                );

        return EnvironmentResponse.from(environment);
    }

    @GetMapping
    public List<EnvironmentResponse> getEnvironments(
            @PathVariable Long projectId,
            Authentication authentication
    ) {
        return environmentService
                .getEnvironments(
                        projectId,
                        authentication.getName()
                )
                .stream()
                .map(EnvironmentResponse::from)
                .toList();
    }
}