package com.divyanshu.knowledgehub.application;

import com.divyanshu.knowledgehub.domain.model.KnowledgeSource;

import java.util.UUID;

public interface AddKnowledgeUseCase {
    KnowledgeSource add(KnowledgeSource source);

    KnowledgeSource get(UUID id);

    KnowledgeSource get(Integer page);
}
