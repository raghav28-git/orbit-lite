package com.orbit.backend.controller;

import com.orbit.backend.dto.ProjectResponse;
import com.orbit.backend.dto.CreateProjectRequest;
import com.orbit.backend.entity.Project;
import com.orbit.backend.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse createProject(
        @RequestBody CreateProjectRequest request,
        Authentication authentication
    ) {
            Project project = projectService.createProject(
                    request,
                    authentication.getName()
            );

            return ProjectResponse.from(project);
        }

    @GetMapping
    public List<ProjectResponse> getMyProjects(
        Authentication authentication
) {
    return projectService
            .getMyProjects(authentication.getName())
            .stream()
            .map(ProjectResponse::from)
            .toList();
}
    @GetMapping("/{projectId}")
public ProjectResponse getProject(
        @PathVariable Long projectId,
        Authentication authentication
) {
    Project project = projectService.getProject(
            projectId,
            authentication.getName()
    );

    return ProjectResponse.from(project);
}
}