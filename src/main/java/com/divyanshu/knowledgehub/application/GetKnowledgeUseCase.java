package com.divyanshu.knowledgehub.application;

import com.divyanshu.knowledgehub.domain.model.KnowledgeSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GetKnowledgeUseCase {
    KnowledgeSource get(UUID id);

    Page<KnowledgeSource> getAll(Pageable pageable);
}
