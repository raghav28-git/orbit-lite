package com.orbit.backend.repository;

import com.orbit.backend.entity.Environment;
import com.orbit.backend.entity.EnvironmentApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnvironmentApiKeyRepository
        extends JpaRepository<EnvironmentApiKey, Long> {

    Optional<EnvironmentApiKey> findByKeyHashAndActiveTrue(
            String keyHash
    );

    Optional<EnvironmentApiKey> findByIdAndEnvironment(
            Long id,
            Environment environment
    );
}