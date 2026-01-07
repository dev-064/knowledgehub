package com.divyanshu.knowledgehub.domain.model;

import java.time.Instant;
import java.util.UUID;

import com.divyanshu.knowledgehub.domain.exception.InvalidKnowledgeException;

public class KnowledgeSource {

    private final UUID id;
    private final SourceType type;
    private final String content;
    private final String sourceUrl;
    private final Instant createdAt;

    public KnowledgeSource(
            UUID id,
            SourceType type,
            String content,
            String sourceUrl,
            Instant createdAt
    ) {
        if (id == null) {
            throw new InvalidKnowledgeException("Knowledge id must not be null");
        }

        if (type == null) {
            throw new InvalidKnowledgeException("Source type must not be null");
        }

        if (type == SourceType.LINK && (sourceUrl == null || sourceUrl.isBlank())) {
            throw new InvalidKnowledgeException("LINK must have a valid sourceUrl");
        }

        if (createdAt == null) {
            throw new InvalidKnowledgeException("createdAt must not be null");
        }

        this.id = id;
        this.type = type;
        this.content = content;
        this.sourceUrl = sourceUrl;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public SourceType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
