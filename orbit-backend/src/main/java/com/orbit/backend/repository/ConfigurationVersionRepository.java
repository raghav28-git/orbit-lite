package com.orbit.backend.repository;

import com.orbit.backend.entity.Configuration;
import com.orbit.backend.entity.ConfigurationVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConfigurationVersionRepository
        extends JpaRepository<ConfigurationVersion, Long> {

    List<ConfigurationVersion> findAllByConfigurationOrderByVersionAsc(
            Configuration configuration
    );

    Optional<ConfigurationVersion> findTopByConfigurationOrderByVersionDesc(
            Configuration configuration
    );

    Optional<ConfigurationVersion> findByConfigurationAndVersion(
        Configuration configuration,
        Integer version
    );
}