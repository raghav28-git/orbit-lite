package com.orbit.backend.dto;

import com.orbit.backend.entity.AuditLog;

import java.time.LocalDateTime;

public record AuditLogResponse(
        Long id,
        String userEmail,
        String action,
        String resource,
        Long resourceId,
        String details,
        LocalDateTime createdAt
) {

    public static AuditLogResponse from(AuditLog auditLog) {
        return new AuditLogResponse(
                auditLog.getId(),
                auditLog.getUserEmail(),
                auditLog.getAction(),
                auditLog.getResource(),
                auditLog.getResourceId(),
                auditLog.getDetails(),
                auditLog.getCreatedAt()
        );
    }
}