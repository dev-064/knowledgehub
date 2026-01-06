package com.divyanshu.knowledgehub.application.service;

import com.divyanshu.knowledgehub.application.GetKnowledgeUseCase;
import com.divyanshu.knowledgehub.application.exception.KnowledgeNotFoundException;
import com.divyanshu.knowledgehub.application.port.out.KnowledgeRepository;
import com.divyanshu.knowledgehub.domain.model.KnowledgeSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetKnowledgeService implements GetKnowledgeUseCase {

    private final KnowledgeRepository knowledgeRepository;

    public GetKnowledgeService(KnowledgeRepository knowledgeRepository){
        this.knowledgeRepository = knowledgeRepository;
    }

    @Override
    public KnowledgeSource get(UUID id){
        return knowledgeRepository.get(id).orElseThrow(() -> new KnowledgeNotFoundException(id));
    }

    @Override
    public Page<KnowledgeSource> getAll(Pageable pageable){
        return knowledgeRepository.getAll(pageable);
    }
}
