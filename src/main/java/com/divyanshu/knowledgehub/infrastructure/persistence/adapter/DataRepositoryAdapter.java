package com.divyanshu.knowledgehub.infrastructure.persistence.adapter;

import com.divyanshu.knowledgehub.application.port.out.DataRepository;
import com.divyanshu.knowledgehub.domain.model.Chunks;
import com.divyanshu.knowledgehub.domain.model.Document;
import com.divyanshu.knowledgehub.infrastructure.persistence.entity.ChunksEntity;
import com.divyanshu.knowledgehub.infrastructure.persistence.entity.DocumentEntity;
import com.divyanshu.knowledgehub.infrastructure.persistence.entity.WorkspaceEntity;
import com.divyanshu.knowledgehub.infrastructure.persistence.exception.DatabaseException;
import com.divyanshu.knowledgehub.infrastructure.persistence.exception.DuplicateEntityException;
import com.divyanshu.knowledgehub.infrastructure.persistence.exception.EntityNotFoundException;
import com.divyanshu.knowledgehub.infrastructure.persistence.repository.JpaChunkRepository;
import com.divyanshu.knowledgehub.infrastructure.persistence.repository.JpaDataRepository;
import com.divyanshu.knowledgehub.infrastructure.persistence.repository.JpaWorkspaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.divyanshu.knowledgehub.domain.model.DocumentUploadStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class DataRepositoryAdapter implements DataRepository {

    private static final Logger log = LoggerFactory.getLogger(DataRepositoryAdapter.class);

    private final JpaDataRepository jpaDataRepository;
    private final JpaChunkRepository jpaChunkRepository;
    private final JpaWorkspaceRepository jpaWorkspaceRepository;

    public DataRepositoryAdapter(
            JpaDataRepository jpaDataRepository,
            JpaChunkRepository jpaChunkRepository,
            JpaWorkspaceRepository jpaWorkspaceRepository
    ) {
        this.jpaDataRepository = jpaDataRepository;
        this.jpaChunkRepository = jpaChunkRepository;
        this.jpaWorkspaceRepository = jpaWorkspaceRepository;
    }

    @Override
    @Transactional
    public Document save(Document doc, List<String> chunks) {
        log.info("Saving document '{}' for workspaceId: {}", doc.getTitle(), doc.getWorkspaceId());
        try {
            WorkspaceEntity workspaceEntity = jpaWorkspaceRepository.findById(doc.getWorkspaceId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Workspace not found with id: " + doc.getWorkspaceId(), null
                    ));

            DocumentEntity documentEntity = new DocumentEntity(
                    doc.getTitle(),
                    doc.getSourceUrl(),
                    doc.getDocumentType(),
                    doc.getContentHash(),
                    doc.getUploadedReference(),
                    doc.getStatus(),
                    workspaceEntity,
                    doc.getCreatedAt(),
                    doc.getUpdatedAt()
            );

            DocumentEntity savedDocument = jpaDataRepository.save(documentEntity);
            log.info("Document saved with id: {}", savedDocument.getId());

            Instant now = Instant.now();
            List<ChunksEntity> chunkEntities = new ArrayList<>(chunks.size());
            for (int i = 0; i < chunks.size(); i++) {
                String chunkText = chunks.get(i);
                chunkEntities.add(new ChunksEntity(
                        chunkText.length() / 4,
                        chunkText,
                        i,
                        new float[0],
                        savedDocument,
                        now,
                        now
                ));
            }

            List<ChunksEntity> savedChunks = jpaChunkRepository.saveAll(chunkEntities);
            log.info("Saved {} chunks for documentId: {}", savedChunks.size(), savedDocument.getId());

            return mapToDomain(savedDocument, savedChunks);

        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException(
                    "Document already exists with contentHash: " + doc.getContentHash(), e);
        } catch (DataAccessException e) {
            throw new DatabaseException(
                    "Database error while saving document: " + doc.getTitle(), e);
        }
    }

    @Override
    public List<Chunks> findChunksByDocumentId(UUID documentId) {
        log.info("Fetching chunks for documentId: {}", documentId);
        try {
            List<ChunksEntity> entities = jpaChunkRepository.findByDocumentIdOrderByChunkIndex(documentId);
            return entities.stream()
                    .map(c -> new Chunks(
                            c.getId(),
                            c.getTokenCount(),
                            c.getChunkText(),
                            c.getChunkIndex(),
                            c.getEmbeddingVector(),
                            c.getCreatedAt(),
                            c.getUpdatedAt()
                    ))
                    .toList();
        } catch (DataAccessException e) {
            throw new DatabaseException("Error fetching chunks for document: " + documentId, e);
        }
    }

    @Override
    @Transactional
    public void updateChunkEmbeddings(List<UUID> chunkIds, List<float[]> embeddings) {
        log.info("Updating embeddings for {} chunks", chunkIds.size());
        try {
            Instant now = Instant.now();
            List<ChunksEntity> chunks = jpaChunkRepository.findAllById(chunkIds);
            if (chunks.size() != chunkIds.size()) {
                throw new EntityNotFoundException(
                        "Some chunks were not found. Expected " + chunkIds.size() + ", found " + chunks.size(), null
                );
            }
            // Re-order to match chunkIds order
            java.util.Map<UUID, ChunksEntity> chunkMap = new java.util.HashMap<>();
            chunks.forEach(c -> chunkMap.put(c.getId(), c));
            for (int i = 0; i < chunkIds.size(); i++) {
                ChunksEntity chunk = chunkMap.get(chunkIds.get(i));
                chunk.setEmbeddingVector(embeddings.get(i));
                chunk.setUpdatedAt(now);
            }
            jpaChunkRepository.saveAll(chunks);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error updating chunk embeddings", e);
        }
    }

    @Override
    @Transactional
    public void updateDocumentStatus(UUID documentId, DocumentUploadStatus status) {
        log.info("Updating document {} status to {}", documentId, status);
        try {
            DocumentEntity doc = jpaDataRepository.findById(documentId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Document not found with id: " + documentId, null
                    ));
            doc.setStatus(status);
            doc.setUpdatedAt(Instant.now());
            jpaDataRepository.save(doc);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error updating document status: " + documentId, e);
        }
    }

    private Document mapToDomain(DocumentEntity docEntity, List<ChunksEntity> chunkEntities) {
        List<Chunks> chunks = chunkEntities.stream()
                .map(c -> new Chunks(
                        c.getId(),
                        c.getTokenCount(),
                        c.getChunkText(),
                        c.getChunkIndex(),
                        c.getEmbeddingVector(),
                        c.getCreatedAt(),
                        c.getUpdatedAt()
                ))
                .toList();

        return new Document(
                docEntity.getId(),
                docEntity.getWorkspaceId(),
                docEntity.getTitle(),
                docEntity.getSourceUrl(),
                docEntity.getType(),
                docEntity.getContentHash(),
                docEntity.getUploadedReference(),
                docEntity.getStatus(),
                docEntity.getCreatedAt(),
                docEntity.getUpdatedAt(),
                chunks
        );
    }
}
