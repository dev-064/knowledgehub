CREATE TABLE document (
    id                  UUID            NOT NULL PRIMARY KEY,
    title               VARCHAR(255)    NOT NULL,
    source_url          VARCHAR(2048),
    type                VARCHAR(50)     NOT NULL,
    content_hash        VARCHAR(512)    NOT NULL,
    uploaded_reference  VARCHAR(1024),
    status              VARCHAR(50)     NOT NULL,
    workspace_id        UUID            NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_document_workspace        FOREIGN KEY (workspace_id) REFERENCES workspace (id),
    CONSTRAINT uq_document_content_hash     UNIQUE (content_hash)
);
