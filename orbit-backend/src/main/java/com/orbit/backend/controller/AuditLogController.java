package com.orbit.backend.controller;

import com.orbit.backend.dto.AuditLogResponse;
import com.orbit.backend.service.AuditLogService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        "/api/v1/projects/{projectId}/environments/{environmentId}/audit-logs"
)
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(
            AuditLogService auditLogService
    ) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public List<AuditLogResponse> getAuditLogs(
            @PathVariable Long projectId,
            @PathVariable Long environmentId,
            Authentication authentication
    ) {
        return auditLogService
                .getEnvironmentLogs(
                        projectId,
                        environmentId,
                        authentication.getName()
                )
                .stream()
                .map(AuditLogResponse::from)
                .toList();
    }
}