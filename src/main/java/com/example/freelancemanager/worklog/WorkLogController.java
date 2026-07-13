package com.example.freelancemanager.worklog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class WorkLogController {
    
    private final WorkLogService workLogService;

    public WorkLogController(WorkLogService workLogService) {
        this.workLogService = workLogService;
    }

    @PostMapping("/api/projects/{projectId}/work-logs")
    @ResponseStatus(HttpStatus.CREATED)
    public WorkLogResponse create(
        @PathVariable Long projectId,
        @Valid @RequestBody WorkLogCreateRequest request
    ) {
        return workLogService.create(projectId, request);
    }

    @GetMapping("/api/projects/{projectId}/work-logs")
    public List<WorkLogResponse> findByProjectId(@PathVariable Long projectId) {
        return workLogService.findByProjectId(projectId);
    }

    @GetMapping("/api/work-logs/{id}")
    public WorkLogResponse findById(@PathVariable Long id) {
        return workLogService.findById(id);
    }

    @PutMapping("/api/work-logs/{id}")
    public WorkLogResponse update(
        @PathVariable Long id,
        @Valid @RequestBody WorkLogUpdateRequest request
    ) {
        return workLogService.update(id, request);
    }

    @DeleteMapping("/api/work-logs/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        workLogService.delete(id);
    }
}
