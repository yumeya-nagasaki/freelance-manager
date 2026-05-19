package com.example.freelancemanager.project;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProjectResponse {
    
    private Long id;
    private String name;
    private ContractType contractType;
    private Integer unitPrice;
    private Integer workRate;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus status;
    private String memo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProjectResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.contractType = project.getContractType();
        this.unitPrice = project.getUnitPrice();
        this.workRate = project.getWorkRate();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();
        this.status = project.getStatus();
        this.memo = project.getMemo();
        this.createdAt = project.getCreatedAt();
        this.updatedAt = project.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public Integer getWorkRate() {
        return workRate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public String getMemo() {
        return memo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
