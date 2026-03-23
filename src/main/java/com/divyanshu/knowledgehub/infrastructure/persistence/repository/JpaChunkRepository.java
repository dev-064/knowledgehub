package com.divyanshu.knowledgehub.infrastructure.persistence.repository;

import com.divyanshu.knowledgehub.infrastructure.persistence.entity.ChunksEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaChunkRepository extends JpaRepository<ChunksEntity, UUID> {

    List<ChunksEntity> findByDocumentIdOrderByChunkIndex(UUID documentId);
}
