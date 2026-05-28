package com.example.freelancemanager.project;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.freelancemanager.client.Client;
import com.example.freelancemanager.client.ClientRepository;
import com.example.freelancemanager.common.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
public class ProjectServiceTest {
    
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void create_正常にProjectを登録できる() {
        ProjectCreateRequest request = new ProjectCreateRequest(
            1L,
            "Example案件",
            ContractType.FIXED_PRICE,
            100000,
            100,
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31),
            ProjectStatus.ACTIVE,
            "初回登録"
        );

        Client client = new Client(
            "Example株式会社",
            "contact@example.com",
            "テストクライアント"
        );

        Project savedProject = new Project(
            client,
            "Example案件",
            ContractType.FIXED_PRICE,
            100000,
            100,
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31),
            ProjectStatus.ACTIVE,
            "初回登録"
        );

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(projectRepository.save(notNull(Project.class))).thenReturn(savedProject);

        ProjectResponse response = projectService.create(request);

        assertThat(response.getName()).isEqualTo("Example案件");
        assertThat(response.getContractType()).isEqualTo(ContractType.FIXED_PRICE);
        assertThat(response.getUnitPrice()).isEqualTo(100000);
        assertThat(response.getWorkRate()).isEqualTo(100);
        assertThat(response.getStartDate()).isEqualTo(LocalDate.of(2026, 1, 1));
        assertThat(response.getEndDate()).isEqualTo(LocalDate.of(2026, 12, 31));
        assertThat(response.getStatus()).isEqualTo(ProjectStatus.ACTIVE);
        assertThat(response.getMemo()).isEqualTo("初回登録");
    }

    @Test
    void findAll_Project一覧を取得できる() {

        Client client = new Client(
            "Example株式会社",
            "contact@example.com",
            "テストクライアント"
        );

        Project project = new Project(
            client,
            "Example案件",
            ContractType.FIXED_PRICE,
            100000,
            100,
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31),
            ProjectStatus.ACTIVE,
            "初回登録"
        );

        when(projectRepository.findAll()).thenReturn(List.of(project));

        List<ProjectResponse> response = projectService.findAll();

        assertThat(response).hasSize(1);
        assertThat(response.get(0).getName()).isEqualTo("Example案件");
    }

    @Test
    void findById_存在するIDの場合Projectを取得できる() {

        Client client = new Client(
            "Example株式会社",
            "contact@example.com",
            "テストクライアント"
        );

        Project project = new Project(
            client,
            "Example案件",
            ContractType.FIXED_PRICE,
            100000,
            100,
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31),
            ProjectStatus.ACTIVE,
            "初回登録"
        );

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectResponse response = projectService.findById(1L);

        assertThat(response.getName()).isEqualTo("Example案件");
    }

    @Test
    void findById_存在しないIDの場合NotFoundExceptionが発生する() {
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.findById(999L))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("project not found. id=999");
    }

    @Test
    void update_存在するIDの場合Projectを更新できる() {

        Client client = new Client(
            "Example株式会社",
            "contact@example.com",
            "テストクライアント"
        );

        Project project = new Project(
            client,
            "Example案件",
            ContractType.FIXED_PRICE,
            100000,
            100,
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31),
            ProjectStatus.ACTIVE,
            "初回登録"
        );

        ProjectUpdateRequest request = new ProjectUpdateRequest(
            "Updated案件",
            ContractType.FIXED_PRICE,
            100000,
            100,
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31),
            ProjectStatus.ACTIVE,
            "更新済み"
        );

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectResponse response = projectService.update(1L, request);

        assertThat(response.getName()).isEqualTo("Updated案件");
        assertThat(response.getMemo()).isEqualTo("更新済み");
    }

    @Test
    void delete_存在するIDの場合Projectを削除できる() {

        when(projectRepository.existsById(1L)).thenReturn(true);

        projectService.delete(1L);

        verify(projectRepository).deleteById(1L);
    }

    @Test
    void delete_存在しないIDの場合NotFoundExceptionが発生する() {
        when(projectRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> projectService.delete(999L))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("project not found. id=999");
    }
}
