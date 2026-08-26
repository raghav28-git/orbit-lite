package com.orbit.backend.service;

import com.orbit.backend.entity.Environment;
import com.orbit.backend.entity.EnvironmentApiKey;
import com.orbit.backend.repository.EnvironmentApiKeyRepository;
import com.orbit.backend.repository.EnvironmentRepository;
import com.orbit.backend.repository.ProjectRepository;
import com.orbit.backend.repository.UserRepository;
import com.orbit.backend.entity.Project;
import com.orbit.backend.entity.User;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import com.orbit.backend.exception.ResourceNotFoundException;
import java.util.List;

@Service
public class EnvironmentApiKeyService {

    private final EnvironmentApiKeyRepository apiKeyRepository;
    private final EnvironmentRepository environmentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

public EnvironmentApiKeyService(
        EnvironmentApiKeyRepository apiKeyRepository,
        EnvironmentRepository environmentRepository,
        ProjectRepository projectRepository,
        UserRepository userRepository,
        AuditLogService auditLogService
) {
    this.apiKeyRepository = apiKeyRepository;
    this.environmentRepository = environmentRepository;
    this.projectRepository = projectRepository;
    this.userRepository = userRepository;
    this.auditLogService = auditLogService;
}

    public String generateApiKey(
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

        String rawKey = generateRandomKey();
        String keyHash = hashKey(rawKey);

        EnvironmentApiKey apiKey = new EnvironmentApiKey(
                keyHash,
                environment,
                true,
                LocalDateTime.now()
        );

        apiKeyRepository.save(apiKey);
        
        auditLogService.log(
        userEmail,
        environment,
        "CREATE_API_KEY",
        "API_KEY",
        apiKey.getId(),
        "Created environment API key"
);

        return rawKey;
    }

    private String generateRandomKey() {
        byte[] randomBytes = new byte[32];

        new SecureRandom().nextBytes(randomBytes);

        return "orbit_"
                + Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(randomBytes);
    }

    private String hashKey(String key) {
        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(
                            key.getBytes(StandardCharsets.UTF_8)
                    );

            StringBuilder hex = new StringBuilder();

            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "SHA-256 algorithm unavailable",
                    exception
            );
        }
    }

    public void revokeApiKey(
        Long projectId,
        Long environmentId,
        Long apiKeyId,
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

    EnvironmentApiKey apiKey =
            apiKeyRepository
                    .findByIdAndEnvironment(apiKeyId, environment)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
        "API key not found"
)
                    );

    if (!apiKey.isActive()) {
        throw new IllegalArgumentException(
                "API key already revoked"
        );
    }

    apiKey.setActive(false);
    apiKey.setRevokedAt(LocalDateTime.now());

    apiKeyRepository.save(apiKey);

    auditLogService.log(
        userEmail,
        environment,
        "REVOKE_API_KEY",
        "API_KEY",
        apiKey.getId(),
        "Revoked environment API key"
);
}

public List<EnvironmentApiKey> getApiKeys(
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

    return apiKeyRepository
            .findAllByEnvironmentOrderByCreatedAtDesc(environment);
}
}