package com.example.freelancemanager.project;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private ContractType contractType;

    @Column(nullable = false)
    @Min(0)
    private Integer unitPrice;

    @Column(nullable = false)
    @Min(1)
    @Max(100)
    private Integer workRate;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private ProjectStatus status;

    @Column(length = 1000)
    private String memo;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected Project() {

    }

    public Project(String name, ContractType contractType, Integer unitPrice, Integer workRate, 
        LocalDate startDate, LocalDate endDate, ProjectStatus status, String memo) 
    {
        this.name = name;
        this.contractType = contractType;
        this.unitPrice = unitPrice;
        this.workRate = workRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.memo = memo;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    public void update(String name, ContractType contractType, Integer unitPrice, Integer workRate, 
        LocalDate startDate, LocalDate endDate, ProjectStatus status, String memo) 
    {
        this.name = name;
        this.contractType = contractType;
        this.unitPrice = unitPrice;
        this.workRate = workRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.memo = memo;
        this.updatedAt = LocalDateTime.now();
    }
}
