package com.divyanshu.knowledgehub.application.port.out;

import com.divyanshu.knowledgehub.domain.model.KnowledgeSource;

import java.util.UUID;

public interface KnowledgeRepository {
    KnowledgeSource save(KnowledgeSource source);

    KnowledgeSource get(UUID id);

    KnowledgeSource get(Integer page);
}

