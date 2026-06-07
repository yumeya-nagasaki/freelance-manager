package com.example.freelancemanager.worklog;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WorkLogCreateRequest {
    
    @NotNull
    private LocalDate workDate;

    @NotNull
    @DecimalMin(value = "0.25")
    private BigDecimal hours;

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
