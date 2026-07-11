package com.example.freelancemanager.client;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.freelancemanager.common.ConflictException;
import com.example.freelancemanager.common.NotFoundException;
import com.example.freelancemanager.project.ProjectRepository;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;
    private final ProjectRepository projectRepository;

    public ClientService(ClientRepository clientRepository, ProjectRepository projectRepository) {
        this.clientRepository = clientRepository;
        this.projectRepository = projectRepository;
    }

    public ClientResponse create(ClientCreateRequest request) {
        Client client = new Client(request.getName(), request.getEmail(), request.getMemo());
        Client savedClient = clientRepository.save(client);

        return new ClientResponse(savedClient);
    }

    @Transactional(readOnly = true)
    public List<ClientResponse> findAll() {
        return clientRepository.findAll()
                .stream()
                .map(ClientResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClientResponse findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("client not found. id=" + id));
        
        return new ClientResponse(client);
    }

    public ClientResponse update(Long id, ClientUpdateRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("client not found. id=" + id));
        
        // トランザクション内で取得したEntityのため、JPAのdirty checkingにより更新される
        client.update(request.getName(), request.getEmail(), request.getMemo());
        
        return new ClientResponse(client);
    }

    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new NotFoundException("client not found. id=" + id);
        }

        if (projectRepository.existsByClientId(id)) {
            throw new ConflictException("client has projects. id=" + id);
        }

        clientRepository.deleteById(id);
    }
}
