package com.example.freelancemanager.project;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "案件の状態", example = "PREPARING")
public enum ProjectStatus {
    
    PREPARING,
    ACTIVE,
    SUSPENDED,
    COMPLETED,
    CANCELED;
}
