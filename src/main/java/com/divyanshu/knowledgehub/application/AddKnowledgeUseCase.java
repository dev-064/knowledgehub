package com.divyanshu.knowledgehub.application;

import com.divyanshu.knowledgehub.domain.model.KnowledgeSource;

public interface AddKnowledgeUseCase {
    KnowledgeSource add(KnowledgeSource source);
}