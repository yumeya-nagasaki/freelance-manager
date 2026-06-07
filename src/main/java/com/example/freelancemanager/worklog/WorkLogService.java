package com.example.freelancemanager.worklog;

import org.springframework.lang.NonNull;
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

    public WorkLogResponse create(@NonNull Long projectId, WorkLogCreateRequest request) {
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
    public WorkLogResponse findById(@NonNull Long id) {
        WorkLog workLog = workLogRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("work log not found. id=" + id));

        return new WorkLogResponse(workLog);
    }

    public WorkLogResponse update(@NonNull Long id, WorkLogUpdateRequest request) {
        WorkLog workLog = workLogRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("work log not found. id=" + id));

        workLog.update(
            request.getWorkDate(),
            request.getHours(),
            request.getDescription()
        );

        return new WorkLogResponse(workLog);
    }

    public void delete(@NonNull Long id) {
        if (!workLogRepository.existsById(id)) {
            throw new NotFoundException("work log not found. id=" + id);
        }

        workLogRepository.deleteById(id);
    }
}
