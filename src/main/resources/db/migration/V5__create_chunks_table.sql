CREATE TABLE chunks (
    id                UUID    NOT NULL PRIMARY KEY,
    token_count       INTEGER,
    chunk_text        TEXT,
    chunk_index       INTEGER,
    embedding_vector  vector(768),
    document_id       UUID    NOT NULL,
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at        TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_chunks_document FOREIGN KEY (document_id) REFERENCES document (id)
);
