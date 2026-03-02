package com.divyanshu.knowledgehub.controller.response.data;

import com.divyanshu.knowledgehub.domain.model.DocumentType;
import com.divyanshu.knowledgehub.domain.model.DocumentUploadStatus;

import java.time.Instant;
import java.util.UUID;

public class SaveDataResponse {
    private final UUID id;
    private final String title;
    private final String source_url;
    private final DocumentType type;
    private final String uploaded_reference;
    private final DocumentUploadStatus status;
    private final Instant created_at;
    private final Instant updated_at;

    public SaveDataResponse(UUID id, String title, String source_url, DocumentType type, String uploaded_reference, DocumentUploadStatus status, Instant created_at, Instant updated_at) {
        this.id = id;
        this.title = title;
        this.source_url = source_url;
        this.type = type;
        this.uploaded_reference = uploaded_reference;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getSource_url() { return source_url; }
    public DocumentType getType() { return type; }
    public String getUploaded_reference() { return uploaded_reference; }
    public DocumentUploadStatus getStatus() { return status; }
    public Instant getCreated_at() { return created_at; }
    public Instant getUpdated_at() { return updated_at; }
}
