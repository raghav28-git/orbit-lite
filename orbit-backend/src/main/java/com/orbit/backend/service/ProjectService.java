package com.orbit.backend.service;

import com.orbit.backend.dto.CreateProjectRequest;
import com.orbit.backend.entity.Project;
import com.orbit.backend.entity.User;
import com.orbit.backend.repository.ProjectRepository;
import com.orbit.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            UserRepository userRepository
    ) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Project createProject(
            CreateProjectRequest request,
            String userEmail
    ) {
        User owner = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        Project project = new Project(
                request.getName(),
                request.getDescription(),
                owner,
                LocalDateTime.now()
        );

        return projectRepository.save(project);
    }

    public List<Project> getMyProjects(String userEmail) {

        User owner = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        return projectRepository.findAllByOwner(owner);
    }

    public Project getProject(
        Long projectId,
        String userEmail
        ) {
                User owner = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                    new IllegalArgumentException("User not found")
            );

                return projectRepository.findByIdAndOwner(projectId, owner)
                .orElseThrow(() ->
                    new IllegalArgumentException("Project not found")
            );
        }
}