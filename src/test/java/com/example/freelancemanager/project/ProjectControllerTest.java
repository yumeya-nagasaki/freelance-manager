package com.example.freelancemanager.project;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        Project project = new Project(
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
    void create_endDateがstartDateより前の場合400BadRequestを返す() throws Exception {
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                        {
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
}
