package com.example.freelancemanager.project;

import java.time.LocalDate;

import com.example.freelancemanager.project.validation.ProjectDateRangeRequest;
import com.example.freelancemanager.project.validation.ValidProjectDateRange;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@ValidProjectDateRange
public class ProjectUpdateRequest implements ProjectDateRangeRequest {
    
    @Schema(description = "案件名", example = "案件ABC")
    @NotBlank
    @Size(max = 100)
    private String name;

    @Schema(description = "契約タイプ", example = "FIXED_PRICE")
    @NotNull
    private ContractType contractType;

    @Schema(description = "単価", example = "10000")
    @NotNull
    @Min(0)
    private Integer unitPrice;

    @Schema(description = "稼働率", example = "100")
    @NotNull
    @Min(1)
    @Max(100)
    private Integer workRate;

    @Schema(description = "開始日", example = "2026-01-01")
    @NotNull
    private LocalDate startDate;

    @Schema(description = "終了日", example = "2026-01-01")
    private LocalDate endDate;

    @Schema(description = "ステータス", example = "PREPARING")
    @NotNull
    private ProjectStatus status;

    @Schema(description = "備考", example = "この案件は、ABC案件です。")
    @Size(max = 1000)
    private String memo;

    public ProjectUpdateRequest() {
        // Jackson用にデフォルトコンストラクタを残す
    }

    public ProjectUpdateRequest(String name, ContractType contractType, Integer unitPrice, Integer workRate, 
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
