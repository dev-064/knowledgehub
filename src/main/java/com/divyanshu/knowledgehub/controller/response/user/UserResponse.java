package com.divyanshu.knowledgehub.controller.response.user;

import com.divyanshu.knowledgehub.domain.model.User;

import java.time.Instant;
import java.util.UUID;

public class UserResponse {
    private final UUID id;
    private final String name;
    private final String email;
    private final Instant createdAt;
    private final Instant updatedAt;

    public UserResponse(UUID id, String name, String email, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
