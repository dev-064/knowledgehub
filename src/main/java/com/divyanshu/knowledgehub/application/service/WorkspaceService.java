package com.divyanshu.knowledgehub.application.service;

import com.divyanshu.knowledgehub.application.exception.WorkspaceAlreadyExistsException;
import com.divyanshu.knowledgehub.application.exception.WorkspaceNotFoundException;
import com.divyanshu.knowledgehub.application.exception.WorkspacePersistenceException;
import com.divyanshu.knowledgehub.application.port.out.WorkspaceRepository;
import com.divyanshu.knowledgehub.domain.model.Workspace;
import com.divyanshu.knowledgehub.infrastructure.persistence.exception.DuplicateEntityException;
import com.divyanshu.knowledgehub.infrastructure.persistence.exception.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class WorkspaceService {
    private static final Logger log = LoggerFactory.getLogger(WorkspaceService.class);

    private final WorkspaceRepository workspaceRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    public Workspace createWorkspace(String name, UUID userId) {
        log.debug("Creating workspace '{}' for userId: {}", name, userId);

        Workspace newWorkspace = new Workspace(
                UUID.randomUUID(),
                userId,
                name,
                List.of(),
                Instant.now(),
                Instant.now()
        );

        try {
            Workspace saved = workspaceRepository.save(newWorkspace);
            log.info("Workspace created with id: {} for userId: {}", saved.getId(), userId);
            return saved;
        } catch (DuplicateEntityException e) {
            log.warn("Workspace creation failed - name already exists: '{}' for userId: {}", name, userId);
            throw new WorkspaceAlreadyExistsException(name);
        } catch (PersistenceException e) {
            log.error("Failed to create workspace '{}' for userId: {}", name, userId, e);
            throw new WorkspacePersistenceException(name);
        }
    }

    public Workspace getWorkspace(UUID id, UUID userId) {
        log.debug("Fetching workspace with id: {} for userId: {}", id, userId);

        try {
            return workspaceRepository.get(id)
                    .orElseThrow(() -> {
                        log.warn("Workspace not found with id: {}", id);
                        return new WorkspaceNotFoundException(id);
                    });
        } catch (PersistenceException e) {
            log.error("Failed to fetch workspace with id: {} for userId: {}", id, userId, e);
            throw new WorkspacePersistenceException(id.toString());
        }
    }
}
