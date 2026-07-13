package com.example.freelancemanager.worklog;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.example.freelancemanager.client.Client;
import com.example.freelancemanager.common.NotFoundException;
import com.example.freelancemanager.project.ContractType;
import com.example.freelancemanager.project.Project;
import com.example.freelancemanager.project.ProjectStatus;

@WebMvcTest(WorkLogController.class)
public class WorkLogControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WorkLogService workLogService;

    @Test
    void create_正常なリクエストの場合201Createdを返す() throws Exception {
        Project project = createProject(1L);
        WorkLog workLog = createWorkLog(1L, project);

        when(workLogService.create(eq(1L), any(WorkLogCreateRequest.class)))
            .thenReturn(new WorkLogResponse(workLog));

        mockMvc.perform(post("/api/projects/1/work-logs")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content("""
                {
                    "workDate": "2026-06-03",
                    "hours": 7.5,
                    "description": "案件管理APIのWorkLog実装"
                }
                """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.projectId").value(1))
            .andExpect(jsonPath("$.workDate").value("2026-06-03"))
            .andExpect(jsonPath("$.hours").value(7.5))
            .andExpect(jsonPath("$.description").value("案件管理APIのWorkLog実装"));
    }

    @Test
    void findByProjectId_存在するProjectIdの場合WorkLog一覧を取得できる() throws Exception {
        Project project = createProject(1L);
        WorkLog workLog = createWorkLog(1L, project);

        when(workLogService.findByProjectId(eq(1L)))
            .thenReturn(List.of(new WorkLogResponse(workLog)));
        
        mockMvc.perform(get("/api/projects/1/work-logs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].projectId").value(1))
            .andExpect(jsonPath("$[0].workDate").value("2026-06-03"))
            .andExpect(jsonPath("$[0].hours").value(7.5))
            .andExpect(jsonPath("$[0].description").value("案件管理APIのWorkLog実装"));
    }

    @Test
    void findByProjectId_WorkLogが存在しない場合空配列を返す() throws Exception {
        when(workLogService.findByProjectId(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/projects/1/work-logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void findById_存在するIDの場合WorkLogを取得できる() throws Exception {
        Project project = createProject(1L);
        WorkLog workLog = createWorkLog(1L, project);

        when(workLogService.findById(1L))
                .thenReturn(new WorkLogResponse(workLog));

        mockMvc.perform(get("/api/work-logs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.projectId").value(1))
                .andExpect(jsonPath("$.workDate").value("2026-06-03"))
                .andExpect(jsonPath("$.hours").value(7.5))
                .andExpect(jsonPath("$.description").value("案件管理APIのWorkLog実装"));
    }

    @Test
    void update_正常なリクエストの場合WorkLogを更新できる() throws Exception {
        Project project = createProject(1L);
        WorkLog workLog = new WorkLog(
                project,
                LocalDate.of(2026, 6, 4),
                new BigDecimal("8.0"),
                "WorkLog更新"
        );
        ReflectionTestUtils.setField(workLog, "id", 1L);

        when(workLogService.update(eq(1L), any(WorkLogUpdateRequest.class)))
                .thenReturn(new WorkLogResponse(workLog));

        mockMvc.perform(put("/api/work-logs/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                  "workDate": "2026-06-04",
                                  "hours": 8.0,
                                  "description": "WorkLog更新"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.projectId").value(1))
                .andExpect(jsonPath("$.workDate").value("2026-06-04"))
                .andExpect(jsonPath("$.hours").value(8.0))
                .andExpect(jsonPath("$.description").value("WorkLog更新"));
    }

    @Test
    void delete_存在するIDの場合204NoContentを返す() throws Exception {
        mockMvc.perform(delete("/api/work-logs/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void create_存在しないProjectIdの場合404NotFoundを返す() throws Exception {
        when(workLogService.create(eq(999L), any(WorkLogCreateRequest.class)))
                .thenThrow(new NotFoundException("Project not found. id=999"));

        mockMvc.perform(post("/api/projects/999/work-logs")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                  "workDate": "2026-06-03",
                                  "hours": 7.5,
                                  "description": "案件管理APIのWorkLog実装"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Project not found. id=999"))
                .andExpect(jsonPath("$.path").value("/api/projects/999/work-logs"));
    }

    @Test
    void findById_存在しないIDの場合404NotFoundを返す() throws Exception {
        when(workLogService.findById(999L))
                .thenThrow(new NotFoundException("WorkLog not found. id=999"));

        mockMvc.perform(get("/api/work-logs/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("WorkLog not found. id=999"))
                .andExpect(jsonPath("$.path").value("/api/work-logs/999"));
    }

    @Test
    void create_workDateがnullの場合400BadRequestを返す() throws Exception {
        mockMvc.perform(post("/api/projects/1/work-logs")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                  "hours": 7.5,
                                  "description": "案件管理APIのWorkLog実装"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.path").value("/api/projects/1/work-logs"));
    }

    @Test
    void create_hoursが下限未満の場合400BadRequestを返す() throws Exception {
        mockMvc.perform(post("/api/projects/1/work-logs")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                  "workDate": "2026-06-03",
                                  "hours": 0,
                                  "description": "案件管理APIのWorkLog実装"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.path").value("/api/projects/1/work-logs"));
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
            "テスト案件"
        );
        ReflectionTestUtils.setField(project, "id", id);
        return project;
    }

    private Client createClient(long id) {
        Client client = new Client(
            "Example株式会社",
            "contact@example.com",
            "テストクライアント"
        );
        ReflectionTestUtils.setField(client, "id", id);
        return client;
    }
}
