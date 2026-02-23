package com.divyanshu.knowledgehub.domain.model;

import com.divyanshu.knowledgehub.domain.exception.InvalidDataException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Document {
    private final UUID id;
    private final String title;
    private final String source_url;
    private final DocumentType type;
    private final String content_hash;
    private final String uploaded_reference;
    private final DocumentUploadStatus status;
    private final Instant created_at;
    private final Instant updated_at;
    private final List<Chunks> chunks;

    public Document(
            UUID id,
            String title,
            String source_url,
            DocumentType type,
            String content_hash,
            String uploaded_reference,
            DocumentUploadStatus status,
            List<Chunks> chunks,
            Instant created_at,
            Instant updated_at
    ) {

        if (id == null) {
            throw new InvalidDataException("Document id must not be null");
        }

        if (title == null) {
            throw new InvalidDataException("Title must not be null");
        }

        if (type == null) {
            throw new InvalidDataException("Type must not be null");
        }

        if (status == null) {
            throw new InvalidDataException("Status must not be null");
        }

        if (type == DocumentType.LINK && source_url == null) {
            throw new InvalidDataException("Source Url cannot be null for Link document");
        }

        if (content_hash == null) {
            throw new InvalidDataException("content hash must not be null");
        }

        if (chunks.isEmpty()) {
            throw new InvalidDataException("chunk must not be empty");
        }

        this.id = id;
        this.title = title;
        this.source_url = source_url;
        this.type = type;
        this.content_hash = content_hash;
        this.uploaded_reference = uploaded_reference;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.chunks = chunks;
    }

    public UUID getId() {
        return this.id;
    }

    public String getSourceUrl() {
        return this.source_url;
    }

    public DocumentType getDocumentType() {
        return this.type;
    }

    public String getContentHash() {
        return this.content_hash;
    }

    public String getUploadedReference() {
        return this.uploaded_reference;
    }

    public DocumentUploadStatus getStatus() {
        return this.status;
    }

    public String getTitle() {
        return this.title;
    }

    public List<Chunks> getChunks() {
        return this.chunks;
    }

    public Instant getCreatedAt() {
        return this.created_at;
    }

    public Instant getUpdatedAt() {
        return this.updated_at;
    }
}
