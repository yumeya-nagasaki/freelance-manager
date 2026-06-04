package com.example.freelancemanager.worklog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {
    
    List<WorkLog> findByProjectId(Long projectId);
}
