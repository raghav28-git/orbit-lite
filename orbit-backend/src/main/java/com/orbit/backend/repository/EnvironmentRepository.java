package com.orbit.backend.repository;

import com.orbit.backend.entity.Environment;
import com.orbit.backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnvironmentRepository
        extends JpaRepository<Environment, Long> {

    List<Environment> findAllByProject(Project project);

    Optional<Environment> findByIdAndProject(
            Long id,
            Project project
    );
}