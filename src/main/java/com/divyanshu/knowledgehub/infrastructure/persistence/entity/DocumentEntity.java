package com.divyanshu.knowledgehub.infrastructure.persistence.entity;

import com.divyanshu.knowledgehub.domain.model.DocumentType;
import com.divyanshu.knowledgehub.domain.model.DocumentUploadStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "document")
public class DocumentEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String sourceUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType type;

    @Column(nullable = false)
    private String contentHash;

    private String uploadedReference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentUploadStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private WorkspaceEntity workspace;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<ChunksEntity> chunks;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected DocumentEntity() {
        // required by JPA
    }

    public DocumentEntity(
            String title,
            String sourceUrl,
            DocumentType type,
            String contentHash,
            String uploadedReference,
            DocumentUploadStatus status,
            WorkspaceEntity workspace,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.title = title;
        this.sourceUrl = sourceUrl;
        this.type = type;
        this.contentHash = contentHash;
        this.uploadedReference = uploadedReference;
        this.status = status;
        this.workspace = workspace;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public DocumentType getType() {
        return type;
    }

    public String getContentHash() {
        return contentHash;
    }

    public String getUploadedReference() {
        return uploadedReference;
    }

    public DocumentUploadStatus getStatus() {
        return status;
    }

    public WorkspaceEntity getWorkspace() {
        return workspace;
    }

    public UUID getWorkspaceId() {
        return workspace != null ? workspace.getId() : null;
    }

    public List<ChunksEntity> getChunks() {
        return chunks;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
