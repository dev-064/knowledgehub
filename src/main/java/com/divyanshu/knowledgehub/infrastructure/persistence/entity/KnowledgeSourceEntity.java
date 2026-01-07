package com.divyanshu.knowledgehub.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

import com.divyanshu.knowledgehub.domain.model.SourceType;

@Entity
@Table(name = "knowledge_source")
public class KnowledgeSourceEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SourceType type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String sourceUrl;

    @Column(nullable = false)
    private Instant createdAt;

    protected KnowledgeSourceEntity() {
        // required by JPA
    }

    public KnowledgeSourceEntity(
            SourceType type,
            String content,
            String sourceUrl,
            Instant createdAt
    ) {
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
