package com.divyanshu.knowledgehub.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "chunks")
public class ChunksEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private Integer tokenCount;

    @Column(columnDefinition = "TEXT")
    private String chunkText;

    private Integer chunkIndex;

    @Column(columnDefinition = "vector(768)")
    private float[] embeddingVector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private DocumentEntity document;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected ChunksEntity() {
        // required by JPA
    }

    public ChunksEntity(
            Integer tokenCount,
            String chunkText,
            Integer chunkIndex,
            float[] embeddingVector,
            DocumentEntity document,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.tokenCount = tokenCount;
        this.chunkText = chunkText;
        this.chunkIndex = chunkIndex;
        this.embeddingVector = embeddingVector;
        this.document = document;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public Integer getTokenCount() {
        return tokenCount;
    }

    public String getChunkText() {
        return chunkText;
    }

    public Integer getChunkIndex() {
        return chunkIndex;
    }

    public float[] getEmbeddingVector() {
        return embeddingVector;
    }

    public DocumentEntity getDocument() {
        return document;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setEmbeddingVector(float[] embeddingVector) {
        this.embeddingVector = embeddingVector;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
