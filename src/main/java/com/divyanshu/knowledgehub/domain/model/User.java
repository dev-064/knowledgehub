package com.divyanshu.knowledgehub.domain.model;

import com.divyanshu.knowledgehub.domain.exception.InvalidDataException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class User {
    private final UUID id;
    private final String name;
    private final String email;
    private final List<Workspace> workspaces;
    private final Instant created_at;
    private final Instant updated_at;

    public User(
            UUID id,
            String name,
            String email,
            List<Workspace> workspaces,
            Instant created_at,
            Instant updated_at
    ) {

        if (id == null) {
            throw new InvalidDataException("User id must not be null");
        }

        if (name == null) {
            throw new InvalidDataException("Name must not be null");
        }

        if (email == null) {
            throw new InvalidDataException("Email must not be null");
        }

        this.id = id;
        this.name = name;
        this.email = email;
        this.workspaces = workspaces;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public List<Workspace> getWorkspaces() {
        return this.workspaces;
    }

    public Instant getCreatedAt() {
        return this.created_at;
    }

    public Instant getUpdatedAt() {
        return this.updated_at;
    }
}
