package com.example.freelancemanager.client;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.freelancemanager.common.NotFoundException;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    @Test
    void create_正常なリクエストの場合201Createdを返す() throws Exception {
        Client client = new Client(
            "Example株式会社",
            "contact@example.com",
            "初回登録"
        );

        when(clientService.create(any(ClientCreateRequest.class)))
            .thenReturn(new ClientResponse(client));
        
        mockMvc.perform(post("/api/clients")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content("""
                    {
                        "name": "Example株式会社",
                        "email": "contact@example.com",
                        "memo": "初回登録"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Example株式会社"))
            .andExpect(jsonPath("$.email").value("contact@example.com"))
            .andExpect(jsonPath("$.memo").value("初回登録"));
    }

    @Test
    void findAll_Client一覧を取得できる() throws Exception {
        Client client = new Client(
            "Example株式会社",
            "contact@example.com",
            "初回登録"
        );

        when(clientService.findAll())
            .thenReturn(List.of(new ClientResponse(client)));
        
        mockMvc.perform(get("/api/clients"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Example株式会社"))
            .andExpect(jsonPath("$[0].email").value("contact@example.com"))
            .andExpect(jsonPath("$[0].memo").value("初回登録"));
    }

    @Test
    void findById_存在するIDの場合Clientを取得できる() throws Exception {
        Client client = new Client(
            "Example株式会社",
            "contact@example.com",
            "初回登録"
        );

        when(clientService.findById(1L))
            .thenReturn(new ClientResponse(client));
        
        mockMvc.perform(get("/api/clients/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Example株式会社"))
            .andExpect(jsonPath("$.email").value("contact@example.com"))
            .andExpect(jsonPath("$.memo").value("初回登録"));
    }

    @Test
    void update_存在するIDの場合Clientを更新できる() throws Exception {
        Client client = new Client(
            "Updated株式会社",
            "updated@example.com",
            "更新済み"
        );

        when(clientService.update(eq(1L), any(ClientUpdateRequest.class)))
            .thenReturn(new ClientResponse(client));
        
        mockMvc.perform(put("/api/clients/1")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content("""
                    {
                        "name": "Updated株式会社",
                        "email": "updated@example.com",
                        "memo": "更新済み"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated株式会社"))
            .andExpect(jsonPath("$.email").value("updated@example.com"))
            .andExpect(jsonPath("$.memo").value("更新済み"));
    }

    @Test
    void delete_存在するIDの場合204NoContentを返す() throws Exception {
        mockMvc.perform(delete("/api/clients/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void findById_存在しないIDの場合404NotFoundを返す() throws Exception {
        when(clientService.findById(999L))
            .thenThrow(new NotFoundException("client not found. id=999"));

        mockMvc.perform(get("/api/clients/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value("client not found. id=999"))
            .andExpect(jsonPath("$.path").value("/api/clients/999"));
    }

    @Test
    void create_nameが空の場合400BadRequestを返す() throws Exception {
        mockMvc.perform(post("/api/clients")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content("""
                    {
                        "name": "",
                        "email": "contact@example.com",
                        "memo": "初回登録"
                    }
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(jsonPath("$.path").value("/api/clients"));
    }
}
