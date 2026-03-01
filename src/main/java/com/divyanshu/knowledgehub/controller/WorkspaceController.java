package com.divyanshu.knowledgehub.controller;

import com.divyanshu.knowledgehub.application.service.WorkspaceService;
import com.divyanshu.knowledgehub.controller.request.workspace.CreateWorkspaceRequest;
import com.divyanshu.knowledgehub.domain.model.Workspace;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/workspace")
public class WorkspaceController {
    private static final Logger log = LoggerFactory.getLogger(WorkspaceController.class);

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Workspace createWorkspace(
            @Valid @RequestBody CreateWorkspaceRequest request,
            HttpServletRequest httpRequest
    ) {
        UUID userId = (UUID) httpRequest.getAttribute("userId");
        log.debug("Received createWorkspace request for userId: {}", userId);

        Workspace workspace = workspaceService.createWorkspace(request.getName(), userId);
        log.info("Workspace created with id: {} for userId: {}", workspace.getId(), userId);

        return workspace;
    }

    @GetMapping("/{id}")
    public Workspace getWorkspace(@PathVariable UUID id, HttpServletRequest httpRequest) {
        UUID userId = (UUID) httpRequest.getAttribute("userId");
        return workspaceService.getWorkspace(id,userId);
    }
}
