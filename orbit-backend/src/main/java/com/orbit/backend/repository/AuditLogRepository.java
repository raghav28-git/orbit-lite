package com.orbit.backend.repository;

import com.orbit.backend.entity.AuditLog;
import com.orbit.backend.entity.Environment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findAllByEnvironmentOrderByCreatedAtDesc(
            Environment environment
    );
}