package com.example.freelancemanager.project;

import java.util.List;
import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.freelancemanager.client.Client;
import com.example.freelancemanager.client.ClientRepository;
import com.example.freelancemanager.common.NotFoundException;

@Service
@Transactional
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final ClientRepository clientRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            ClientRepository clientRepository
    ) {
        this.projectRepository = projectRepository;
        this.clientRepository = clientRepository;
    }

    public ProjectResponse create(ProjectCreateRequest request) {

        Long clientId = Objects.requireNonNull(request.getClientId());
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new NotFoundException("client not found. id=" + clientId));
        
        Project project = new Project(
            client, 
            request.getName(), 
            request.getContractType(), 
            request.getUnitPrice(), 
            request.getWorkRate(), 
            request.getStartDate(), 
            request.getEndDate(), 
            request.getStatus(), 
            request.getMemo()
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

    @Transactional(readOnly = true)
    public List<ProjectResponse> findByClientId(@NonNull Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new NotFoundException("client not found. id=" + clientId);
        }

        return projectRepository.findByClientId(clientId)
                .stream()
                .map(ProjectResponse::new)
                .toList();
    }
}
