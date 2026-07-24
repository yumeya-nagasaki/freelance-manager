package com.example.freelancemanager.client;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.freelancemanager.project.ProjectResponse;
import com.example.freelancemanager.project.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@Tag(
    name = "Clients",
    description = "取引先を管理するAPI"
)
@RestController
@RequestMapping("/api/clients")
public class ClientController {
    
    private final ClientService clientService;
    private final ProjectService projectService;

    public ClientController(ClientService clientService, ProjectService projectService) {
        this.clientService = clientService;
        this.projectService = projectService;
    }

    @Operation(summary = "取引先を登録する")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponse create(@Valid @RequestBody ClientCreateRequest request) {
        return clientService.create(request);
    }

    @Operation(summary = "取引先一覧を取得する")
    @GetMapping
    public List<ClientResponse> findAll() {
        return clientService.findAll();
    }

    @Operation(summary = "取引先を取得する")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "取得成功",
            useReturnTypeSchema = true
        ),
        @ApiResponse(
            responseCode = "404",
            description = "指定された取引先が存在しない",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ClientResponse findById(@Parameter(description = "取引先ID", example = "1") @PathVariable Long id) {
        return clientService.findById(id);
    }
    
    @Operation(summary = "取引先を更新する")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "更新成功",
            useReturnTypeSchema = true
        ),
        @ApiResponse(
            responseCode = "404",
            description = "指定された取引先が存在しない",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public ClientResponse update(@Parameter(description = "取引先ID", example = "1") @PathVariable Long id, @Valid @RequestBody ClientUpdateRequest request) {
        return clientService.update(id, request);
    }

    @Operation(summary = "取引先を削除する")
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "削除成功",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "指定された取引先が存在しない",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "関連する案件が存在するため削除できない",
            content = @Content
        )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "取引先ID", example = "1") @PathVariable Long id) {
        clientService.delete(id);
    }

    @Operation(summary = "取引先の案件一覧を取得する")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "取得成功",
            useReturnTypeSchema = true
        ),
        @ApiResponse(
            responseCode = "404",
            description = "指定された取引先が存在しない",
            content = @Content
        )
    })
    @GetMapping("/{id}/projects")
    public List<ProjectResponse> findProjectsByClientId(@Parameter(description = "取引先ID", example = "1") @PathVariable Long id) {
        return projectService.findByClientId(id);
    }
}
