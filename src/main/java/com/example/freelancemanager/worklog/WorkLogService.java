package com.example.freelancemanager.worklog;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.freelancemanager.common.NotFoundException;
import com.example.freelancemanager.project.Project;
import com.example.freelancemanager.project.ProjectRepository;

@Service
@Transactional
public class WorkLogService {
    
    private final WorkLogRepository workLogRepository;
    private final ProjectRepository projectRepository;

    public WorkLogService(WorkLogRepository workLogRepository, ProjectRepository projectRepository) {
        this.workLogRepository = workLogRepository;
        this.projectRepository = projectRepository;
    }

    public WorkLogResponse create(Long projectId, WorkLogCreateRequest request) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new NotFoundException("project not found. id=" + projectId));

        WorkLog workLog = new WorkLog(
            project,
            request.getWorkDate(),
            request.getHours(),
            request.getDescription()
        );

        WorkLog savedWorkLog = workLogRepository.save(workLog);

        return new WorkLogResponse(savedWorkLog);
    }

    @Transactional(readOnly = true)
    public WorkLogResponse findById(Long id) {
        WorkLog workLog = workLogRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("work log not found. id=" + id));

        return new WorkLogResponse(workLog);
    }

    public WorkLogResponse update(Long id, WorkLogUpdateRequest request) {
        WorkLog workLog = workLogRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("work log not found. id=" + id));

        workLog.update(
            request.getWorkDate(),
            request.getHours(),
            request.getDescription()
        );

        return new WorkLogResponse(workLog);
    }

    @Transactional(readOnly = true)
    public List<WorkLogResponse> findByProjectId(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new NotFoundException("project not found. id=" + projectId);
        }

        return workLogRepository.findByProjectId(projectId)
            .stream()
            .map(WorkLogResponse::new)
            .toList();
    }

    public void delete(Long id) {
        if (!workLogRepository.existsById(id)) {
            throw new NotFoundException("work log not found. id=" + id);
        }

        workLogRepository.deleteById(id);
    }
}
