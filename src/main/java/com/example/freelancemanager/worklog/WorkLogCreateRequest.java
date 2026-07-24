package com.example.freelancemanager.worklog;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WorkLogCreateRequest {
    
    @Schema(description = "作業日", example = "2026-01-01")
    @NotNull
    private LocalDate workDate;

    @Schema(description = "作業時間", example = "1.0")
    @NotNull
    @DecimalMin(value = "0.25")
    private BigDecimal hours;

    @Schema(description = "備考", example = "この作業は、ABC作業です。")
    @Size(max = 1000)
    private String description;

    public WorkLogCreateRequest() {
    }

    public WorkLogCreateRequest(LocalDate workDate, BigDecimal hours, String description) {
        this.workDate = workDate;
        this.hours = hours;
        this.description = description;
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
}
