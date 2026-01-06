package com.divyanshu.knowledgehub.application.service;

import com.divyanshu.knowledgehub.application.AddKnowledgeUseCase;
import com.divyanshu.knowledgehub.application.exception.KnowledgePersistenceException;
import com.divyanshu.knowledgehub.application.port.out.KnowledgeRepository;
import com.divyanshu.knowledgehub.domain.model.KnowledgeSource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddKnowledgeService implements AddKnowledgeUseCase {

    private final KnowledgeRepository knowledgeRepository;

    public AddKnowledgeService(KnowledgeRepository knowledgeRepository) {
        this.knowledgeRepository = knowledgeRepository;
    }

    @Override
    public KnowledgeSource add(KnowledgeSource source) {
        try{
            KnowledgeSource processedSource = switch (source.getType()) {
                case TEXT -> source;
                case LINK -> {
                    String content = knowledgeRepository.fetchUrlContent(source.getSourceUrl());
                    yield new KnowledgeSource(
                        source.getId(),
                        source.getType(),
                        content,
                        source.getSourceUrl(),
                        source.getCreatedAt()
                    );
                }
            };

            return knowledgeRepository.save(processedSource);
        } catch (Exception e) {
            throw new KnowledgePersistenceException("Failed to persist source", e);
        }
    }
}
