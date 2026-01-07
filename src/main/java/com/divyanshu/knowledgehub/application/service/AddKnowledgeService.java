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
            return knowledgeRepository.save(source);
        } catch (Exception e) {
            throw new KnowledgePersistenceException("Failed to persist source", e);
        }
    }

    @Override
    public KnowledgeSource get(UUID id) {

    }

    @Override
    public KnowledgeSource get(Integer page) {

    }

}
