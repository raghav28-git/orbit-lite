package com.orbit.backend.dto;

public class RollbackConfigurationRequest {

    private Integer version;

    public RollbackConfigurationRequest() {
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}