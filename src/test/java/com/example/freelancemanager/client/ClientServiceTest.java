package com.example.freelancemanager.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.freelancemanager.common.ConflictException;
import com.example.freelancemanager.common.NotFoundException;
import com.example.freelancemanager.project.ProjectRepository;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void create_正常にClientを登録できる() {
        ClientCreateRequest request = new ClientCreateRequest(
                "Example株式会社",
                "contact@example.com",
                "初回登録");

        Client savedClient = new Client(
                "Example株式会社",
                "contact@example.com",
                "初回登録");

        when(clientRepository.save(notNull(Client.class))).thenReturn(savedClient);

        ClientResponse response = clientService.create(request);

        assertThat(response.getName()).isEqualTo("Example株式会社");
        assertThat(response.getEmail()).isEqualTo("contact@example.com");
        assertThat(response.getMemo()).isEqualTo("初回登録");
    }

    @Test
    void findAll_Client一覧を取得できる() {
        Client client = new Client(
                "Example株式会社",
                "contact@example.com",
                "初回登録");

        when(clientRepository.findAll()).thenReturn(List.of(client));

        List<ClientResponse> response = clientService.findAll();

        assertThat(response).hasSize(1);
        assertThat(response.get(0).getName()).isEqualTo("Example株式会社");
    }

    @Test
    void findById_存在するIDの場合Clientを取得できる() {
        Client client = new Client(
                "Example株式会社",
                "contact@example.com",
                "初回登録");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ClientResponse response = clientService.findById(1L);

        assertThat(response.getName()).isEqualTo("Example株式会社");
    }

    @Test
    void findById_存在しないIDの場合NotFoundExceptionが発生する() {
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.findById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("client not found. id=999");
    }

    @Test
    void update_存在するIDの場合Clientを更新できる() {
        Client client = new Client(
                "Example株式会社",
                "contact@example.com",
                "初回登録");

        ClientUpdateRequest request = new ClientUpdateRequest(
                "Updated株式会社",
                "updated@example.com",
                "更新済み");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ClientResponse response = clientService.update(1L, request);

        assertThat(response.getName()).isEqualTo("Updated株式会社");
        assertThat(response.getEmail()).isEqualTo("updated@example.com");
        assertThat(response.getMemo()).isEqualTo("更新済み");
    }

    @Test
    void delete_存在するIDかつProjectが存在しない場合Clientを削除できる() {
        when(clientRepository.existsById(1L)).thenReturn(true);
        when(projectRepository.existsByClientId(1L)).thenReturn(false);

        clientService.delete(1L);

        verify(clientRepository).deleteById(1L);
    }

    @Test
    void delete_存在しないIDの場合NotFoundExceptionが発生する() {
        when(clientRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> clientService.delete(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("client not found. id=999");
    }

    @Test
    void delete_Projectが存在するClientの場合ConflictExceptionが発生する() {
        when(clientRepository.existsById(1L)).thenReturn(true);
        when(projectRepository.existsByClientId(1L)).thenReturn(true);

        assertThatThrownBy(() -> clientService.delete(1L))
                .isInstanceOf(ConflictException.class)
                .hasMessage("client has projects. id=1");

        verify(clientRepository, never()).deleteById(1L);
    }
}
