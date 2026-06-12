package com.example.freelancemanager.worklog;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class WorkLogResponse {
    
    private Long id;
    private Long projectId;
    private LocalDate workDate;
    private BigDecimal hours;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public WorkLogResponse(WorkLog workLog) {
        this.id = workLog.getId();
        this.projectId = workLog.getProject().getId();
        this.workDate = workLog.getWorkDate();
        this.hours = workLog.getHours();
        this.description = workLog.getDescription();
        this.createdAt = workLog.getCreatedAt();
        this.updatedAt = workLog.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public BigDecimal getHours() {
        return hours;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
