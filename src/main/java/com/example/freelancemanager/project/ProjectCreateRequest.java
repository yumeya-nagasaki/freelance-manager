package com.example.freelancemanager.project;

import java.time.LocalDate;

import com.example.freelancemanager.project.validation.ProjectDateRangeRequest;
import com.example.freelancemanager.project.validation.ValidProjectDateRange;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@ValidProjectDateRange
public class ProjectCreateRequest implements ProjectDateRangeRequest {
    
    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    private ContractType contractType;

    @NotNull
    @Min(0)
    private Integer unitPrice;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer workRate;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    private ProjectStatus status;

    @Size(max = 1000)
    private String memo;

    public ProjectCreateRequest() {
        // Jackson用にデフォルトコンストラクタを残す
    }

    public ProjectCreateRequest(String name, ContractType contractType, Integer unitPrice, Integer workRate, 
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
    
}
