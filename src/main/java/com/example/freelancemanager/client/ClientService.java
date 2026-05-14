package com.example.freelancemanager.client;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.freelancemanager.common.NotFoundException;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
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
    public ClientResponse findById(@NonNull Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("client not found. id=" + id));
        
        return new ClientResponse(client);
    }

    public ClientResponse update(@NonNull Long id, ClientUpdateRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("client not found. id=" + id));
        
        // トランザクション内で取得したEntityのため、JPAのdirty checkingにより更新される
        client.update(request.getName(), request.getEmail(), request.getMemo());
        
        return new ClientResponse(client);
    }

    public void delete(@NonNull Long id) {
        if (!clientRepository.existsById(id)) {
            throw new NotFoundException("client not found. id=" + id);
        }

        clientRepository.deleteById(id);
    }
}
