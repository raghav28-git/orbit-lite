package com.orbit.backend.service;

import com.orbit.backend.entity.AuditLog;
import com.orbit.backend.entity.Environment;
import com.orbit.backend.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(
            AuditLogRepository auditLogRepository
    ) {
        this.auditLogRepository = auditLogRepository;
    }

    public AuditLog log(
            String userEmail,
            Environment environment,
            String action,
            String resource,
            Long resourceId,
            String details
    ) {
        AuditLog auditLog = new AuditLog(
                userEmail,
                environment,
                action,
                resource,
                resourceId,
                details,
                LocalDateTime.now()
        );

        return auditLogRepository.save(auditLog);
    }

    public List<AuditLog> getEnvironmentLogs(
            Environment environment
    ) {
        return auditLogRepository
                .findAllByEnvironmentOrderByCreatedAtDesc(environment);
    }
}