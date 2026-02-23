package com.divyanshu.knowledgehub.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Chunks {
    private final UUID id;
    private final Integer token_count;
    private final String chunk_text;
    private final Integer chunk_index;
    private final float[] embedding_vector;
    private final Instant created_at;
    private final Instant updated_at;

    public Chunks(
            UUID id,
            Integer token_count,
            String chunk_text,
            Integer chunk_index,
            float[] embedding_vector,
            Instant created_at,
            Instant updated_at
    ) {

        this.id = id;
        this.token_count = token_count;
        this.chunk_text = chunk_text;
        this.chunk_index = chunk_index;
        this.embedding_vector = embedding_vector;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public UUID getId() {
        return this.id;
    }

    public Integer getTokenCount() {
        return this.token_count;
    }

    public String getChunkText() {
        return this.chunk_text;
    }

    public Integer getChunkIndex() {
        return this.chunk_index;
    }

    public float[] getEmbeddingVector() {
        return this.embedding_vector;
    }

    public Instant getCreatedAt() {
        return this.created_at;
    }

    public Instant getUpdatedAt() {
        return this.updated_at;
    }
}
