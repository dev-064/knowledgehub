package com.divyanshu.knowledgehub.infrastructure.persistence.adapter;

import com.divyanshu.knowledgehub.application.port.out.KnowledgeRepository;
import com.divyanshu.knowledgehub.domain.model.KnowledgeSource;
import com.divyanshu.knowledgehub.infrastructure.persistence.entity.KnowledgeSourceEntity;
import com.divyanshu.knowledgehub.infrastructure.persistence.repository.JpaKnowledgeRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public class KnowledgeRepositoryAdapter implements KnowledgeRepository {

    private final JpaKnowledgeRepository jpaRepository;

    public KnowledgeRepositoryAdapter(JpaKnowledgeRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public KnowledgeSource save(KnowledgeSource source) {

        KnowledgeSourceEntity entity = new KnowledgeSourceEntity(
                source.getType(),
                source.getContent(),
                source.getSourceUrl(),
                source.getCreatedAt()
        );

        KnowledgeSourceEntity saved = jpaRepository.save(entity);

        return new KnowledgeSource(
                saved.getId(),
                saved.getType(),
                saved.getContent(),
                saved.getSourceUrl(),
                saved.getCreatedAt()
        );
    }

    @Override
    public KnowledgeSource get(UUID id) {
        Optional<KnowledgeSourceEntity> entity = jpaRepository.findById(id);
        return entity.get();
    }

}