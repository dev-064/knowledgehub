package com.divyanshu.knowledgehub.infrastructure.persistence.adapter;

import com.divyanshu.knowledgehub.application.port.out.WorkspaceRepository;
import com.divyanshu.knowledgehub.domain.model.Workspace;
import com.divyanshu.knowledgehub.infrastructure.persistence.entity.UserEntity;
import com.divyanshu.knowledgehub.infrastructure.persistence.entity.WorkspaceEntity;
import com.divyanshu.knowledgehub.infrastructure.persistence.exception.DatabaseException;
import com.divyanshu.knowledgehub.infrastructure.persistence.exception.DuplicateEntityException;
import com.divyanshu.knowledgehub.infrastructure.persistence.exception.EntityNotFoundException;
import com.divyanshu.knowledgehub.infrastructure.persistence.repository.JpaUserRepository;
import com.divyanshu.knowledgehub.infrastructure.persistence.repository.JpaWorkspaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class WorkspaceRepositoryAdapter implements WorkspaceRepository {

    private static final Logger log = LoggerFactory.getLogger(WorkspaceRepositoryAdapter.class);

    private final JpaWorkspaceRepository jpaWorkspaceRepository;
    private final JpaUserRepository jpaUserRepository;

    public WorkspaceRepositoryAdapter(
            JpaWorkspaceRepository jpaWorkspaceRepository,
            JpaUserRepository jpaUserRepository
    ) {
        this.jpaWorkspaceRepository = jpaWorkspaceRepository;
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public Workspace save(Workspace workspace) {
        log.info("Saving workspace '{}' for userId: {}", workspace.getName(), workspace.getUserId());
        try {
            UserEntity userEntity = jpaUserRepository.findById(workspace.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "User not found with id: " + workspace.getUserId(), null
                    ));

            WorkspaceEntity entity = new WorkspaceEntity(
                    workspace.getName(),
                    userEntity,
                    workspace.getCreatedAt(),
                    workspace.getUpdatedAt()
            );

            WorkspaceEntity saved = jpaWorkspaceRepository.save(entity);
            log.info("Workspace saved with id: {}", saved.getId());
            return mapToDomain(saved);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Workspace already exists with name: " + workspace.getName(), e);
        } catch (DataAccessException e) {
            throw new DatabaseException("Database error while saving workspace: " + workspace.getName(), e);
        }
    }

    @Override
    public Optional<Workspace> get(UUID id) {
        log.debug("Fetching workspace with id: {}", id);
        try {
            return jpaWorkspaceRepository.findById(id).map(this::mapToDomain);
        } catch (DataAccessException e) {
            throw new DatabaseException("Database error while fetching workspace with id: " + id, e);
        }
    }

    private Workspace mapToDomain(WorkspaceEntity entity) {
        return new Workspace(
                entity.getId(),
                entity.getUser().getId(),
                entity.getName(),
                List.of(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
