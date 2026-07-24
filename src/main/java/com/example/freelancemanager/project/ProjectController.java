package com.example.freelancemanager.project;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    name = "Projects",
    description = "案件を管理するAPI"
)
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "案件を登録する")
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "登録成功",
            useReturnTypeSchema = true
        ),
        @ApiResponse(
            responseCode = "404",
            description = "指定された取引先が存在しない",
            content = @Content
        )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse create(@Valid @RequestBody ProjectCreateRequest request) {
        return projectService.create(request);
    }

    @Operation(summary = "案件一覧を取得する")
    @GetMapping
    public List<ProjectResponse> findAll() {
        return projectService.findAll();
    }

    @Operation(summary = "案件を取得する")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "取得成功",
            useReturnTypeSchema = true
        ),
        @ApiResponse(
            responseCode = "404",
            description = "指定された案件が存在しない",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ProjectResponse findById(@Parameter(description = "案件ID", example = "1") @PathVariable Long id) {
        return projectService.findById(id);
    }
    
    @Operation(summary = "案件を更新する")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "更新成功",
            useReturnTypeSchema = true
        ),
        @ApiResponse(
            responseCode = "404",
            description = "指定された案件が存在しない",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public ProjectResponse update(@Parameter(description = "案件ID", example = "1") @PathVariable Long id, @Valid @RequestBody ProjectUpdateRequest request) {
        return projectService.update(id, request);
    }

    @Operation(summary = "案件を削除する")
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "削除成功",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "指定された案件が存在しない",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "関連する作業記録が存在するため削除できない",
            content = @Content
        )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "案件ID", example = "1") @PathVariable Long id) {
        projectService.delete(id);
    }
}
