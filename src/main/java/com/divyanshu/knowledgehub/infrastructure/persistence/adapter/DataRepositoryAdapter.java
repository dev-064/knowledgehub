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

import com.divyanshu.knowledgehub.domain.model.DocumentType;
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
    public Document save(Document doc) {
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
            jpaDataRepository.flush();
            log.info("Document saved with id: {}", savedDocument.getId());

            return mapToDomain(savedDocument);

        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException(
                    "Document already exists with contentHash: " + doc.getContentHash(), e);
        } catch (DataAccessException e) {
            throw new DatabaseException(
                    "Database error while saving document: " + doc.getTitle(), e);
        }
    }

    @Override
    @Transactional
    public List<Chunks> saveChunks(List<String> chunks, List<float[]> embeddings, UUID documentId) {
        log.info("Saving the chunks for the document ID : {}", documentId);
        try {
            // getReferenceById returns a proxy — avoids a redundant SELECT since we only need the FK reference
            DocumentEntity doc = jpaDataRepository.getReferenceById(documentId);

            Instant now = Instant.now();
            List<ChunksEntity> chunkEntities = new ArrayList<>(chunks.size());
            for (int i = 0; i < chunks.size(); i++) {
                String chunkText = chunks.get(i);
                chunkEntities.add(new ChunksEntity(
                        chunkText.length() / 4,
                        chunkText,
                        i,
                        embeddings.get(i),
                        doc,
                        now,
                        now
                ));
            }

            List<ChunksEntity> savedChunks = jpaChunkRepository.saveAll(chunkEntities);
            log.info("Saved {} chunks for documentId: {}", savedChunks.size(), documentId);

            return mapToDomain(savedChunks);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error saving chunks for document: " + documentId, e);
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
    public void updateDocumentStatus(UUID documentId, DocumentUploadStatus status, String uploadedUrl) {
        log.info("Updating document {} status to {}", documentId, status);
        try {
            DocumentEntity doc = jpaDataRepository.findById(documentId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Document not found with id: " + documentId, null
                    ));
            doc.setStatus(status);
            doc.setUploadedReference(uploadedUrl);
            doc.setUpdatedAt(Instant.now());
            jpaDataRepository.save(doc);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error updating document status: " + documentId, e);
        }
    }

    @Override
    public List<Document> findRelevantDocuments(float[] queryEmbeddings, UUID workspaceId) {
        log.info("Finding relevant chunks for workspaceId: {} with similarity threshold 0.75", workspaceId);
        try {
            String vectorString = toVectorString(queryEmbeddings);
            List<Object[]> rows = jpaChunkRepository.findByWorkspaceIdAboveSimilarityThreshold(
                    vectorString, workspaceId, 0.75
            );
            log.info("Found {} chunks above similarity threshold for workspaceId: {}", rows.size(), workspaceId);
            return rows.stream()
                    .map(this::mapRowToDocument)
                    .toList();
        } catch (DataAccessException e) {
            throw new DatabaseException("Error finding relevant documents for workspace: " + workspaceId, e);
        }
    }

    private String toVectorString(float[] vector) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {
            sb.append(vector[i]);
            if (i < vector.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private Document mapRowToDocument(Object[] row) {
        // chunk fields: row[0]=c.id, row[1]=c.chunk_text, row[2]=c.chunk_index
        UUID chunkId = UUID.fromString(row[0].toString());
        String chunkText = row[1] != null ? row[1].toString() : null;
        Integer chunkIndex = row[2] != null ? ((Number) row[2]).intValue() : null;

        // document fields: row[3..12]
        UUID documentId = UUID.fromString(row[3].toString());
        UUID workspaceId = UUID.fromString(row[4].toString());
        String title = row[5] != null ? row[5].toString() : null;
        String sourceUrl = row[6] != null ? row[6].toString() : null;
        DocumentType type = row[7] != null ? DocumentType.valueOf(row[7].toString()) : null;
        String contentHash = row[8] != null ? row[8].toString() : null;
        String uploadedReference = row[9] != null ? row[9].toString() : null;
        DocumentUploadStatus status = row[10] != null ? DocumentUploadStatus.valueOf(row[10].toString()) : null;
        Instant createdAt = row[11] != null ? ((java.sql.Timestamp) row[11]).toInstant() : null;
        Instant updatedAt = row[12] != null ? ((java.sql.Timestamp) row[12]).toInstant() : null;

        Chunks matchingChunk = new Chunks(chunkId, null, chunkText, chunkIndex, null, createdAt, updatedAt);

        return new Document(documentId, workspaceId, title, sourceUrl, type, contentHash, uploadedReference, status, createdAt, updatedAt, List.of(matchingChunk));
    }

    private Document mapToDomain(DocumentEntity docEntity) {
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
                List.of()
        );
    }

    private List<Chunks> mapToDomain(List<ChunksEntity> chunkEntities) {
        return chunkEntities.stream()
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
    }
}
