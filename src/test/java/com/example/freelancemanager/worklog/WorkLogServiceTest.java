package com.example.freelancemanager.worklog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.freelancemanager.client.Client;
import com.example.freelancemanager.common.NotFoundException;
import com.example.freelancemanager.project.ContractType;
import com.example.freelancemanager.project.Project;
import com.example.freelancemanager.project.ProjectRepository;
import com.example.freelancemanager.project.ProjectStatus;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class WorkLogServiceTest {
    
    @Mock
    private WorkLogRepository workLogRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private WorkLogService workLogService;

    @Test
    void create_存在するProjectIdの場合WorkLogを作成できる() {
        Project project = createProject(1L);

        WorkLogCreateRequest request = new WorkLogCreateRequest(
            LocalDate.of(2026, 6, 3),
            new BigDecimal("7.5"),
            "案件管理APIのWorkLog実装"
        );

        WorkLog savedWorkLog = new WorkLog(
            project,
            request.getWorkDate(),
            request.getHours(),
            request.getDescription()
        );
        ReflectionTestUtils.setField(savedWorkLog, "id", 1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(workLogRepository.save(notNull(WorkLog.class))).thenReturn(savedWorkLog);

        WorkLogResponse response = workLogService.create(1L, request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getProjectId()).isEqualTo(1L);
        assertThat(response.getWorkDate()).isEqualTo(LocalDate.of(2026, 6, 3));
        assertThat(response.getHours()).isEqualByComparingTo(new BigDecimal("7.5"));
        assertThat(response.getDescription()).isEqualTo("案件管理APIのWorkLog実装");
    }

    @Test
    void create_存在しないProjectIdの場合NotFoundExceptionが発生する() {
        WorkLogCreateRequest request = new WorkLogCreateRequest(
            LocalDate.of(2026, 6, 3),
            new BigDecimal("7.5"),
            "案件管理APIのWorkLog実装"
        );

        when(projectRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workLogService.create(999L, request))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("project not found. id=999");

        verify(workLogRepository, never()).save(notNull(WorkLog.class));
    }

    @Test
    void findByProjectId_存在するProjectIdの場合WorkLog一覧を取得できる() {
        Project project = createProject(1L);
        WorkLog workLog = createWorkLog(1L, project);

        when(projectRepository.existsById(1L)).thenReturn(true);
        when(workLogRepository.findByProjectId(1L)).thenReturn(List.of(workLog));

        List<WorkLogResponse> responses = workLogService.findByProjectId(1L);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getId()).isEqualTo(1L);
        assertThat(responses.get(0).getProjectId()).isEqualTo(1L);
        assertThat(responses.get(0).getWorkDate()).isEqualTo(LocalDate.of(2026, 6, 3));
        assertThat(responses.get(0).getHours()).isEqualByComparingTo(new BigDecimal("7.5"));
    }

    @Test
    void findByProjectId_存在しないProjectIdの場合NotFoundExceptionが発生する() {
        when(projectRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> workLogService.findByProjectId(999L))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("project not found. id=999");
    }

    @Test
    void findById_存在するIDの場合WorkLogを取得できる() {
        Project project = createProject(1L);
        WorkLog workLog = createWorkLog(1L, project);

        when(workLogRepository.findById(1L)).thenReturn(Optional.of(workLog));

        WorkLogResponse response = workLogService.findById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getProjectId()).isEqualTo(1L);
        assertThat(response.getDescription()).isEqualTo("案件管理APIのWorkLog実装");
    }

    @Test
    void findById_存在しないIDの場合NotFoundExceptionが発生する() {
        when(workLogRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workLogService.findById(999L))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("work log not found. id=999");
    }

    @Test
    void update_存在するIDの場合WorkLogを更新できる() {
        Project project = createProject(1L);
        WorkLog workLog = createWorkLog(1L, project);

        WorkLogUpdateRequest request = new WorkLogUpdateRequest(
            LocalDate.of(2026, 6, 4),
            new BigDecimal("8.0"),
            "WorkLog更新"
        );

        when(workLogRepository.findById(1L)).thenReturn(Optional.of(workLog));

        WorkLogResponse response = workLogService.update(1L, request);

        assertThat(response.getWorkDate()).isEqualTo(LocalDate.of(2026, 6, 4));
        assertThat(response.getHours()).isEqualByComparingTo(new BigDecimal("8.0"));
        assertThat(response.getDescription()).isEqualTo("WorkLog更新");
    }

    @Test
    void update_存在しないIDの場合NotFoundExceptionが発生する() {
        WorkLogUpdateRequest request = new WorkLogUpdateRequest(
            LocalDate.of(2026, 6, 4),
            new BigDecimal("8.0"),
            "WorkLog更新"
        );

        when(workLogRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workLogService.update(999L, request))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("work log not found. id=999");
    }

    @Test
    void delete_存在するIDの場合WorkLogを削除できる() {
        when(workLogRepository.existsById(1L)).thenReturn(true);

        workLogService.delete(1L);

        verify(workLogRepository).deleteById(1L);
    }

    @Test
    void delete_存在しないIDの場合NotFoundExceptionが発生する() {
        when(workLogRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> workLogService.delete(999L))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("work log not found. id=999");
        
        verify(workLogRepository, never()).deleteById(999L);
    }

    private WorkLog createWorkLog(long id, Project project) {
        WorkLog workLog = new WorkLog(
            project,
            LocalDate.of(2026, 6, 3),
            new BigDecimal("7.5"),
            "案件管理APIのWorkLog実装"
        );
        ReflectionTestUtils.setField(workLog, "id", id);
        return workLog;
    }

    private Project createProject(long id) {
        Project project = new Project(
            createClient(1L), 
            "業務管理システム開発", 
            ContractType.MONTHLY, 
            600000, 
            100, 
            LocalDate.of(2026, 1, 1), 
            LocalDate.of(2026, 12, 31), 
            ProjectStatus.ACTIVE, 
            "初回登録"
        );
        ReflectionTestUtils.setField(project, "id", id);
        return project;
    }

    private Client createClient(long id) {
        Client client = new Client(
            "Example株式会社", 
            "contact@example.com", 
            "初回登録"
        );
        ReflectionTestUtils.setField(client, "id", id);
        return client;
    }
}
