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

@Service
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final EnvironmentRepository environmentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ConfigurationService(
            ConfigurationRepository configurationRepository,
            EnvironmentRepository environmentRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository
    ) {
        this.configurationRepository = configurationRepository;
        this.environmentRepository = environmentRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

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
                        new IllegalArgumentException("Project not found")
                );

        Environment environment = environmentRepository
                .findByIdAndProject(environmentId, project)
                .orElseThrow(() ->
                        new IllegalArgumentException("Environment not found")
                );

        Configuration configuration = new Configuration(
                request.getKey(),
                request.getValue(),
                environment,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        return configurationRepository.save(configuration);
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
                        new IllegalArgumentException("Project not found")
                );

        Environment environment = environmentRepository
                .findByIdAndProject(environmentId, project)
                .orElseThrow(() ->
                        new IllegalArgumentException("Environment not found")
                );

        return configurationRepository
                .findAllByEnvironment(environment);
    }

    private User getUser(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );
    }
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
                    new IllegalArgumentException("Project not found")
            );

    Environment environment = environmentRepository
            .findByIdAndProject(environmentId, project)
            .orElseThrow(() ->
                    new IllegalArgumentException("Environment not found")
            );

    Configuration configuration = configurationRepository
            .findByIdAndEnvironment(configurationId, environment)
            .orElseThrow(() ->
                    new IllegalArgumentException("Configuration not found")
            );

    configuration.setValue(request.getValue());
    configuration.setUpdatedAt(LocalDateTime.now());

    return configurationRepository.save(configuration);
}
}