package com.example.freelancemanager.project;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.freelancemanager.common.NotFoundException;

@Service
@Transactional
public class ProjectService {
    
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectResponse create(ProjectCreateRequest request) {
        Project project = new Project(request.getName(), request.getContractType(), request.getUnitPrice(), request.getWorkRate(), 
            request.getStartDate(), request.getEndDate(), request.getStatus(), request.getMemo()
        );
        Project savedProject = projectRepository.save(project);
        return new ProjectResponse(savedProject);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> findAll() {
        return projectRepository.findAll()
                .stream()
                .map(ProjectResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse findById(@NonNull Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("project not found. id=" + id));
        return new ProjectResponse(project);
    }

    public ProjectResponse update(@NonNull Long id, ProjectUpdateRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("project not found. id=" + id));

        // トランザクション内で取得したEntityのため、JPAのdirty checkingにより更新される
        project.update(request.getName(), request.getContractType(), request.getUnitPrice(), request.getWorkRate(), 
            request.getStartDate(), request.getEndDate(), request.getStatus(), request.getMemo()
        );

        return new ProjectResponse(project);
    }

    public void delete(@NonNull Long id) {
        if (!projectRepository.existsById(id)) {
            throw new NotFoundException("project not found. id=" + id);
        }

        projectRepository.deleteById(id);
    }
}
