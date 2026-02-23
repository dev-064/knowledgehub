package com.divyanshu.knowledgehub.domain.model;

import com.divyanshu.knowledgehub.domain.exception.InvalidDataException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Workspace {
    private final UUID id;
    private final String name;
    private final List<Document> documents;
    private final Instant created_at;
    private final Instant updated_at;

    public Workspace (
            UUID id,
            String name,
            List<Document> documents,
            Instant created_at,
            Instant updated_at
    ) {

        if (id == null) {
            throw new InvalidDataException("Workspace id must not be null");
        }

        if (name == null) {
            throw new InvalidDataException("Name must not be null");
        }

        this.id = id;
        this.name = name;
        this.documents = documents;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<Document> getDocuments() {
        return this.documents;
    }

    public Instant getCreatedAt() {
        return this.created_at;
    }

    public Instant getUpdatedAt() {
        return this.updated_at;
    }
}
