package com.divyanshu.knowledgehub.application.port.out;

import com.divyanshu.knowledgehub.domain.model.KnowledgeSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface KnowledgeRepository {
    KnowledgeSource save(KnowledgeSource source);

    Optional<KnowledgeSource> get(UUID id);

    Page<KnowledgeSource> getAll(Pageable pageable);

}

