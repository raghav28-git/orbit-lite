package com.orbit.backend.repository;

import com.orbit.backend.entity.Configuration;
import com.orbit.backend.entity.Environment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConfigurationRepository
        extends JpaRepository<Configuration, Long> {

    List<Configuration> findAllByEnvironment(
            Environment environment
    );

    Optional<Configuration> findByIdAndEnvironment(
            Long id,
            Environment environment
    );

    Optional<Configuration> findByEnvironmentAndKey(
            Environment environment,
            String key
    );
}