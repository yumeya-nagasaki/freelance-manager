package com.example.freelancemanager.project;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.freelancemanager.client.Client;
import com.example.freelancemanager.common.ConflictException;
import com.example.freelancemanager.common.NotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @Test
    void create_正常なリクエストの場合201Createdを返す() throws Exception {

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
                "初回登録");

        when(projectService.create(any(ProjectCreateRequest.class)))
                .thenReturn(new ProjectResponse(project));

        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                        {
                            "clientId": 1,
                            "name": "Example案件",
                            "contractType": "FIXED_PRICE",
                            "unitPrice": 100000,
                            "workRate": 100,
                            "startDate": "2026-01-01",
                            "endDate": "2026-12-31",
                            "status": "ACTIVE",
                            "memo": "初回登録"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Example案件"))
                .andExpect(jsonPath("$.contractType").value("FIXED_PRICE"))
                .andExpect(jsonPath("$.unitPrice").value(100000))
                .andExpect(jsonPath("$.workRate").value(100))
                .andExpect(jsonPath("$.startDate").value("2026-01-01"))
                .andExpect(jsonPath("$.endDate").value("2026-12-31"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.memo").value("初回登録"));
    }

    @Test
    void update_存在するIDの場合Projectを更新できる() throws Exception {

        Client client = new Client(
                "Example株式会社",
                "contact@example.com",
                "テストクライアント"
        );

        Project project = new Project(
                client,
                "Updated案件",
                ContractType.FIXED_PRICE,
                100000,
                100,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31),
                ProjectStatus.ACTIVE,
                "更新済み"
        );

        when(projectService.update(eq(1L), any(ProjectUpdateRequest.class)))
                .thenReturn(new ProjectResponse(project));

        mockMvc.perform(put("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                        {
                            "name": "Updated案件",
                            "contractType": "FIXED_PRICE",
                            "unitPrice": 100000,
                            "workRate": 100,
                            "startDate": "2026-01-01",
                            "endDate": "2026-12-31",
                            "status": "ACTIVE",
                            "memo": "更新済み"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated案件"))
                .andExpect(jsonPath("$.contractType").value("FIXED_PRICE"))
                .andExpect(jsonPath("$.unitPrice").value(100000))
                .andExpect(jsonPath("$.workRate").value(100))
                .andExpect(jsonPath("$.startDate").value("2026-01-01"))
                .andExpect(jsonPath("$.endDate").value("2026-12-31"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.memo").value("更新済み"));
    }

    @Test
    void findAll_Project一覧を取得できる() throws Exception {

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

        when(projectService.findAll())
                .thenReturn(List.of(new ProjectResponse(project)));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Example案件"))
                .andExpect(jsonPath("$[0].contractType").value("FIXED_PRICE"))
                .andExpect(jsonPath("$[0].unitPrice").value(100000))
                .andExpect(jsonPath("$[0].workRate").value(100))
                .andExpect(jsonPath("$[0].startDate").value("2026-01-01"))
                .andExpect(jsonPath("$[0].endDate").value("2026-12-31"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$[0].memo").value("初回登録"));
    }

    @Test
    void findById_存在するIDの場合Projectを取得できる() throws Exception {

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

        when(projectService.findById(1L))
                .thenReturn(new ProjectResponse(project));

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Example案件"))
                .andExpect(jsonPath("$.contractType").value("FIXED_PRICE"))
                .andExpect(jsonPath("$.unitPrice").value(100000))
                .andExpect(jsonPath("$.workRate").value(100))
                .andExpect(jsonPath("$.startDate").value("2026-01-01"))
                .andExpect(jsonPath("$.endDate").value("2026-12-31"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.memo").value("初回登録"));
    }

    @Test
    void findById_存在しないIDの場合404NotFoundを返す() throws Exception {
        when(projectService.findById(999L))
                .thenThrow(new NotFoundException("project not found. id=999"));

        mockMvc.perform(get("/api/projects/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("project not found. id=999"))
                .andExpect(jsonPath("$.path").value("/api/projects/999"));
    }

    @Test
    void delete_存在するIDの場合204NoContentを返す() throws Exception {
        mockMvc.perform(delete("/api/projects/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void create_nameが空の場合400BadRequestを返す() throws Exception {
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                        {
                            "name": "",
                            "contractType": "FIXED_PRICE",
                            "unitPrice": 100000,
                            "workRate": 100,
                            "startDate": "2026-01-01",
                            "endDate": "2026-12-31",
                            "status": "ACTIVE",
                            "memo": "初回登録"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.path").value("/api/projects"));
    }

    @Test
    void create_endDateがstartDateより前の場合400BadRequestを返す() throws Exception {
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                        {
                          "clientId": 1,
                          "name": "業務管理システム開発",
                          "contractType": "MONTHLY",
                          "unitPrice": 600000,
                          "workRate": 100,
                          "startDate": "2026-06-01",
                          "endDate": "2026-05-31",
                          "status": "ACTIVE",
                          "memo": "終了日が開始日より前"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("終了日は開始日以降の日付にしてください"));
    }

    @Test
    void create_contractTypeに不正な値を指定した場合400BadRequestを返す() throws Exception {
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                        {
                          "clientId": 1,
                          "name": "業務管理システム開発",
                          "contractType": "INVALID",
                          "unitPrice": 600000,
                          "workRate": 100,
                          "startDate": "2026-06-01",
                          "endDate": "2026-12-31",
                          "status": "ACTIVE",
                          "memo": "契約形態が不正"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(
                        "contractType：指定された値が不正です。使用可能な値： FIXED_PRICE, MONTHLY, HOURLY"
                ))
                .andExpect(jsonPath("$.path").value("/api/projects"));
    }

    @Test
    void create_statusに不正な値を指定した場合400BadRequestを返す() throws Exception {
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                        {
                          "clientId": 1,
                          "name": "業務管理システム開発",
                          "contractType": "MONTHLY",
                          "unitPrice": 600000,
                          "workRate": 100,
                          "startDate": "2026-06-01",
                          "endDate": "2026-12-31",
                          "status": "INVALID",
                          "memo": "案件ステータスが不正"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(
                        "status：指定された値が不正です。使用可能な値： PREPARING, ACTIVE, SUSPENDED, COMPLETED, CANCELED"
                ))
                .andExpect(jsonPath("$.path").value("/api/projects"));
    }

    @Test
    void delete_WorkLogが存在するProjectの場合409Conflictを返す() throws Exception {
        doThrow(new ConflictException("project has work logs. id=1"))
                .when(projectService)
                .delete(1L);
        
        mockMvc.perform(delete("/api/projects/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("project has work logs. id=1"))
                .andExpect(jsonPath("$.path").value("/api/projects/1"));
    }
}
