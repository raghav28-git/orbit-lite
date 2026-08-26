package com.orbit.backend.service;

import com.orbit.backend.dto.CreateConfigurationRequest;
import com.orbit.backend.entity.Configuration;
import com.orbit.backend.entity.Environment;
import com.orbit.backend.entity.Project;
import com.orbit.backend.entity.User;
import com.orbit.backend.repository.ConfigurationRepository;
import com.orbit.backend.repository.EnvironmentRepository;
import com.orbit.backend.repository.ProjectRepository;
import com.orbit.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.orbit.backend.dto.UpdateConfigurationRequest;
import java.time.LocalDateTime;
import java.util.List;
import com.orbit.backend.dto.RuntimeConfigurationResponse;
import java.util.Optional;
import com.orbit.backend.repository.ConfigurationVersionRepository;
import com.orbit.backend.entity.ConfigurationVersion;
import java.util.Map;
import java.util.stream.Collectors;
import com.orbit.backend.dto.ConfigurationVersionResponse;
import com.orbit.backend.dto.RollbackConfigurationRequest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import com.orbit.backend.exception.ResourceNotFoundException;

@Service
public class ConfigurationService {

private final ConfigurationRepository configurationRepository;
private final EnvironmentRepository environmentRepository;
private final ProjectRepository projectRepository;
private final UserRepository userRepository;
private final ConfigurationVersionRepository configurationVersionRepository;
private final AuditLogService auditLogService;

public ConfigurationService(
        ConfigurationRepository configurationRepository,
        EnvironmentRepository environmentRepository,
        ProjectRepository projectRepository,
        UserRepository userRepository,
        ConfigurationVersionRepository configurationVersionRepository,
        AuditLogService auditLogService
) {
    this.configurationRepository = configurationRepository;
    this.environmentRepository = environmentRepository;
    this.projectRepository = projectRepository;
    this.userRepository = userRepository;
    this.configurationVersionRepository = configurationVersionRepository;
    this.auditLogService = auditLogService;
}

    @CacheEvict(
        value = "runtimeConfigurations",
        key = "#projectId + ':' + #environmentId"
    )
    public Configuration createConfiguration(
            Long projectId,
            Long environmentId,
            CreateConfigurationRequest request,
            String userEmail
    ) {
        User owner = getUser(userEmail);

        Project project = projectRepository
                .findByIdAndOwner(projectId, owner)
                .orElseThrow(() ->
        new ResourceNotFoundException("Project not found")
);

        Environment environment = environmentRepository
                .findByIdAndProject(environmentId, project)
.orElseThrow(() ->
        new ResourceNotFoundException("Environment not found")
);

        Configuration configuration = new Configuration(
                request.getKey(),
                request.getValue(),
                environment,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Configuration savedConfiguration =
        configurationRepository.save(configuration);

auditLogService.log(
        userEmail,
        environment,
        "CREATE_CONFIGURATION",
        "CONFIGURATION",
        savedConfiguration.getId(),
        "Created configuration: " + savedConfiguration.getKey()
);

return savedConfiguration;
    }

    public List<Configuration> getConfigurations(
            Long projectId,
            Long environmentId,
            String userEmail
    ) {
        User owner = getUser(userEmail);

        Project project = projectRepository
                .findByIdAndOwner(projectId, owner)
.orElseThrow(() ->
        new ResourceNotFoundException("Project not found")
);

        Environment environment = environmentRepository
                .findByIdAndProject(environmentId, project)
                .orElseThrow(() ->
        new ResourceNotFoundException("Environment not found")
);

        return configurationRepository
                .findAllByEnvironment(environment);
    }

    private User getUser(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
        new ResourceNotFoundException("User not found")
);
    }

    @CacheEvict(
        value = "runtimeConfigurations",
        key = "#projectId + ':' + #environmentId"
    )
    public Configuration updateConfiguration(
        Long projectId,
        Long environmentId,
        Long configurationId,
        UpdateConfigurationRequest request,
        String userEmail
) {
    User owner = getUser(userEmail);

    Project project = projectRepository
            .findByIdAndOwner(projectId, owner)
.orElseThrow(() ->
        new ResourceNotFoundException("Project not found")
);

    Environment environment = environmentRepository
            .findByIdAndProject(environmentId, project)
            .orElseThrow(() ->
        new ResourceNotFoundException("Environment not found")
);

    Configuration configuration = configurationRepository
            .findByIdAndEnvironment(configurationId, environment)
            .orElseThrow(() ->
        new ResourceNotFoundException("Configuration not found")
);

    Optional<ConfigurationVersion> latestVersion =
        configurationVersionRepository
                .findTopByConfigurationOrderByVersionDesc(configuration);

int versionNumber = latestVersion
        .map(version -> version.getVersion() + 1)
        .orElse(1);

ConfigurationVersion version = new ConfigurationVersion(
        configuration,
        versionNumber,
        configuration.getValue(),
        LocalDateTime.now()
);

configurationVersionRepository.save(version);

configuration.setValue(request.getValue());
configuration.setUpdatedAt(LocalDateTime.now());

configuration.setValue(request.getValue());
configuration.setUpdatedAt(LocalDateTime.now());

Configuration savedConfiguration =
        configurationRepository.save(configuration);

auditLogService.log(
        userEmail,
        environment,
        "UPDATE_CONFIGURATION",
        "CONFIGURATION",
        savedConfiguration.getId(),
        "Updated configuration: " + savedConfiguration.getKey()
);

return savedConfiguration;
}

@Cacheable(
        value = "runtimeConfigurations",
        key = "#projectId + ':' + #environmentId"
)
public RuntimeConfigurationResponse getRuntimeConfiguration(
        Long projectId,
        Long environmentId,
        String userEmail
) {
    User owner = getUser(userEmail);

    Project project = projectRepository
            .findByIdAndOwner(projectId, owner)
.orElseThrow(() ->
        new ResourceNotFoundException("Project not found")
);

    Environment environment = environmentRepository
            .findByIdAndProject(environmentId, project)
            .orElseThrow(() ->
        new ResourceNotFoundException("Environment not found")
);

    Map<String, String> configurations =
            configurationRepository
                    .findAllByEnvironment(environment)
                    .stream()
                    .collect(Collectors.toMap(
                            Configuration::getKey,
                            Configuration::getValue
                    ));

    return new RuntimeConfigurationResponse(
            environmentId,
            configurations
    );
}
public List<ConfigurationVersionResponse> getConfigurationVersions(
        Long projectId,
        Long environmentId,
        Long configurationId,
        String userEmail
) {
    User owner = getUser(userEmail);

    Project project = projectRepository
            .findByIdAndOwner(projectId, owner)
.orElseThrow(() ->
        new ResourceNotFoundException("Project not found")
);

    Environment environment = environmentRepository
            .findByIdAndProject(environmentId, project)
            .orElseThrow(() ->
        new ResourceNotFoundException("Environment not found")
);

    Configuration configuration = configurationRepository
            .findByIdAndEnvironment(configurationId, environment)
            .orElseThrow(() ->
        new ResourceNotFoundException("Configuration not found")
);

    return configurationVersionRepository
            .findAllByConfigurationOrderByVersionAsc(configuration)
            .stream()
            .map(version -> new ConfigurationVersionResponse(
                    version.getVersion(),
                    version.getValue(),
                    version.getCreatedAt()
            ))
            .toList();
}
        
        @CacheEvict(
        value = "runtimeConfigurations",
        key = "#projectId + ':' + #environmentId"
        )
        public Configuration rollbackConfiguration(
        Long projectId,
        Long environmentId,
        Long configurationId,
        RollbackConfigurationRequest request,
        String userEmail
) {
    User owner = getUser(userEmail);

    Project project = projectRepository
            .findByIdAndOwner(projectId, owner)
.orElseThrow(() ->
        new ResourceNotFoundException("Project not found")
);

    Environment environment = environmentRepository
            .findByIdAndProject(environmentId, project)
            .orElseThrow(() ->
        new ResourceNotFoundException("Environment not found")
);

    Configuration configuration = configurationRepository
            .findByIdAndEnvironment(configurationId, environment)
            .orElseThrow(() ->
        new ResourceNotFoundException("Configuration not found")
);

    ConfigurationVersion targetVersion =
            configurationVersionRepository
                    .findByConfigurationAndVersion(
                            configuration,
                            request.getVersion()
                    )
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Configuration version not found"
                            )
                    );

    ConfigurationVersion latestVersion =
            configurationVersionRepository
                    .findTopByConfigurationOrderByVersionDesc(configuration)
                    .orElse(null);

    int nextVersion = latestVersion == null
            ? 1
            : latestVersion.getVersion() + 1;

    ConfigurationVersion rollbackVersion =
            new ConfigurationVersion(
                    configuration,
                    nextVersion,
                    targetVersion.getValue(),
                    LocalDateTime.now()
            );

    configurationVersionRepository.save(rollbackVersion);

    configuration.setValue(targetVersion.getValue());
configuration.setUpdatedAt(LocalDateTime.now());

return configurationRepository.save(configuration);
}

public RuntimeConfigurationResponse getRuntimeConfiguration(
        Environment environment
) {
    Map<String, String> configurations =
            configurationRepository
                    .findAllByEnvironment(environment)
                    .stream()
                    .collect(Collectors.toMap(
                            Configuration::getKey,
                            Configuration::getValue
                    ));

    return new RuntimeConfigurationResponse(
            environment.getId(),
            configurations
    );
}
}