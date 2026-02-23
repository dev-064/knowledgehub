package com.divyanshu.knowledgehub.domain.model;

import java.time.Instant;
import java.util.UUID;

import com.divyanshu.knowledgehub.domain.exception.InvalidDataException;

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
            throw new InvalidDataException("Knowledge id must not be null");
        }

        if (type == null) {
            throw new InvalidDataException("Source type must not be null");
        }

        if (type == SourceType.LINK && (sourceUrl == null || sourceUrl.isBlank())) {
            throw new InvalidDataException("LINK must have a valid sourceUrl");
        }

        if (content == null || content.isBlank()) {
            throw new InvalidDataException("Content should not be null or an empty string");
        }

        if (createdAt == null) {
            throw new InvalidDataException("createdAt must not be null");
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
