package com.divyanshu.knowledgehub.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workspace")
public class WorkspaceEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL)
    private List<DocumentEntity> documents;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected WorkspaceEntity() {
        // required by JPA
    }

    public WorkspaceEntity(
            String name,
            UserEntity user,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.name = name;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UserEntity getUser() {
        return user;
    }

    public List<DocumentEntity> getDocuments() {
        return documents;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
