package com.divyanshu.knowledgehub.infrastructure.persistence.adapter;

import com.divyanshu.knowledgehub.application.port.out.KnowledgeRepository;
import com.divyanshu.knowledgehub.domain.model.KnowledgeSource;
import com.divyanshu.knowledgehub.infrastructure.persistence.entity.KnowledgeSourceEntity;
import com.divyanshu.knowledgehub.infrastructure.persistence.repository.JpaKnowledgeRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class KnowledgeRepositoryAdapter implements KnowledgeRepository {

    private final JpaKnowledgeRepository jpaRepository;
    private final Logger log = LoggerFactory.getLogger(KnowledgeRepositoryAdapter.class);

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
        log.debug("Saving the knowledgeSource, id={}", entity.getId());
        KnowledgeSourceEntity saved = jpaRepository.save(entity);
        log.debug("Saved the knowledgeSource, id={}", entity.getId());
        return mapToDomain(saved);
    }

    @Override
    public Optional<KnowledgeSource>  get(UUID id) {
        return jpaRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public Page<KnowledgeSource> getAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(this::mapToDomain);
    }

    private KnowledgeSource mapToDomain(KnowledgeSourceEntity entity) {
        return new KnowledgeSource(
                entity.getId(),
                entity.getType(),
                entity.getContent(),
                entity.getSourceUrl(),
                entity.getCreatedAt()
        );
    }
}