CREATE TABLE workspace (
    id          UUID        NOT NULL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    user_id     UUID        NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_workspace_user    FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT uq_workspace_user_name UNIQUE (user_id, name)
);
