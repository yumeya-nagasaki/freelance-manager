package com.example.freelancemanager.worklog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(
    name = "Work Logs",
    description = "作業記録を管理するAPI"
)
@RestController
public class WorkLogController {
    
    private final WorkLogService workLogService;

    public WorkLogController(WorkLogService workLogService) {
        this.workLogService = workLogService;
    }

    @Operation(summary = "作業記録を登録する")
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "登録成功",
            useReturnTypeSchema = true
        ),
        @ApiResponse(
            responseCode = "404",
            description = "指定された案件が存在しない",
            content = @Content
        )
    })
    @PostMapping("/api/projects/{projectId}/work-logs")
    @ResponseStatus(HttpStatus.CREATED)
    public WorkLogResponse create(
        @Parameter(description = "案件ID", example = "1") @PathVariable Long projectId,
        @Valid @RequestBody WorkLogCreateRequest request
    ) {
        return workLogService.create(projectId, request);
    }

    @Operation(summary = "作業記録一覧を取得する")
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
    @GetMapping("/api/projects/{projectId}/work-logs")
    public List<WorkLogResponse> findByProjectId(@Parameter(description = "案件ID", example = "1") @PathVariable Long projectId) {
        return workLogService.findByProjectId(projectId);
    }

    @Operation(summary = "作業記録を取得する")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "取得成功",
            useReturnTypeSchema = true
        ),
        @ApiResponse(
            responseCode = "404",
            description = "指定された作業記録が存在しない",
            content = @Content
        )
    })
    @GetMapping("/api/work-logs/{id}")
    public WorkLogResponse findById(@Parameter(description = "作業記録ID", example = "1") @PathVariable Long id) {
        return workLogService.findById(id);
    }

    @Operation(summary = "作業記録を更新する")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "更新成功",
            useReturnTypeSchema = true
        ),
        @ApiResponse(
            responseCode = "404",
            description = "指定された作業記録が存在しない",
            content = @Content
        )
    })
    @PutMapping("/api/work-logs/{id}")
    public WorkLogResponse update(
        @Parameter(description = "作業記録ID", example = "1") @PathVariable Long id,
        @Valid @RequestBody WorkLogUpdateRequest request
    ) {
        return workLogService.update(id, request);
    }

    @Operation(summary = "作業記録を削除する")
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "削除成功",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "指定された作業記録が存在しない",
            content = @Content
        )
    })
    @DeleteMapping("/api/work-logs/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "作業記録ID", example = "1") @PathVariable Long id) {
        workLogService.delete(id);
    }
}
