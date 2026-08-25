package com.orbit.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "configuration_versions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_configuration_version",
                        columnNames = {"configuration_id", "version"}
                )
        }
)
public class ConfigurationVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "configuration_id", nullable = false)
    private Configuration configuration;

    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String value;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public ConfigurationVersion() {
    }

    public ConfigurationVersion(
            Configuration configuration,
            Integer version,
            String value,
            LocalDateTime createdAt
    ) {
        this.configuration = configuration;
        this.version = version;
        this.value = value;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Integer getVersion() {
        return version;
    }

    public String getValue() {
        return value;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}