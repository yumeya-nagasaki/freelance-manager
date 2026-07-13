package com.example.freelancemanager.worklog;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.freelancemanager.project.Project;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "work_logs")
public class WorkLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private LocalDate workDate;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal hours;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected WorkLog() {
    }

    public WorkLog(Project project, LocalDate workDate, BigDecimal hours, String description) {
        this.project = project;
        this.workDate = workDate;
        this.hours = hours;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }

    public Project getProject() {
        return project;
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

    public void update(LocalDate workDate, BigDecimal hours, String description) {
        this.workDate = workDate;
        this.hours = hours;
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
}
