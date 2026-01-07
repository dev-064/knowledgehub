package com.divyanshu.knowledgehub.infrastructure.persistence.repository;

import com.divyanshu.knowledgehub.infrastructure.persistence.entity.KnowledgeSourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaKnowledgeRepository
        extends JpaRepository<KnowledgeSourceEntity, UUID> {
}
