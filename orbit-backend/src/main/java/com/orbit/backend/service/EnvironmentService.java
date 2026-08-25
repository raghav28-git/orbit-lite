package com.orbit.backend.service;

import com.orbit.backend.dto.CreateEnvironmentRequest;
import com.orbit.backend.entity.Environment;
import com.orbit.backend.entity.Project;
import com.orbit.backend.entity.User;
import com.orbit.backend.repository.EnvironmentRepository;
import com.orbit.backend.repository.ProjectRepository;
import com.orbit.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnvironmentService {

    private final EnvironmentRepository environmentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public EnvironmentService(
            EnvironmentRepository environmentRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository
    ) {
        this.environmentRepository = environmentRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Environment createEnvironment(
            Long projectId,
            CreateEnvironmentRequest request,
            String userEmail
    ) {
        User owner = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        Project project = projectRepository
                .findByIdAndOwner(projectId, owner)
                .orElseThrow(() ->
                        new IllegalArgumentException("Project not found")
                );

        Environment environment = new Environment(
                request.getName(),
                project,
                LocalDateTime.now()
        );

        return environmentRepository.save(environment);
    }

    public List<Environment> getEnvironments(
            Long projectId,
            String userEmail
    ) {
        User owner = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        Project project = projectRepository
                .findByIdAndOwner(projectId, owner)
                .orElseThrow(() ->
                        new IllegalArgumentException("Project not found")
                );

        return environmentRepository.findAllByProject(project);
    }
}