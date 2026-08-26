package com.orbit.backend.service;

import com.orbit.backend.entity.AuditLog;
import com.orbit.backend.entity.Environment;
import com.orbit.backend.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import com.orbit.backend.entity.Environment;
import com.orbit.backend.entity.Project;
import com.orbit.backend.entity.User;
import com.orbit.backend.repository.EnvironmentRepository;
import com.orbit.backend.repository.ProjectRepository;
import com.orbit.backend.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final EnvironmentRepository environmentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public AuditLogService(
        AuditLogRepository auditLogRepository,
        EnvironmentRepository environmentRepository,
        ProjectRepository projectRepository,
        UserRepository userRepository
        ) {
        this.auditLogRepository = auditLogRepository;
        this.environmentRepository = environmentRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
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

    public List<AuditLog> getEnvironmentLogs(
        Long projectId,
        Long environmentId,
        String userEmail
) {
    User owner = userRepository
            .findByEmail(userEmail)
            .orElseThrow(() ->
                    new IllegalArgumentException("User not found")
            );

    Project project = projectRepository
            .findByIdAndOwner(projectId, owner)
            .orElseThrow(() ->
                    new IllegalArgumentException("Project not found")
            );

    Environment environment = environmentRepository
            .findByIdAndProject(environmentId, project)
            .orElseThrow(() ->
                    new IllegalArgumentException("Environment not found")
            );

    return auditLogRepository
            .findAllByEnvironmentOrderByCreatedAtDesc(environment);
}
}