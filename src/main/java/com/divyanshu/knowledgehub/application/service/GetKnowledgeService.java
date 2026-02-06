package com.divyanshu.knowledgehub.application.service;

import com.divyanshu.knowledgehub.application.GetKnowledgeUseCase;
import com.divyanshu.knowledgehub.application.exception.KnowledgeNotFoundException;
import com.divyanshu.knowledgehub.application.port.out.KnowledgeRepository;
import com.divyanshu.knowledgehub.domain.model.KnowledgeSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetKnowledgeService implements GetKnowledgeUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetKnowledgeService.class);

    private final KnowledgeRepository knowledgeRepository;

    public GetKnowledgeService(KnowledgeRepository knowledgeRepository) {
        this.knowledgeRepository = knowledgeRepository;
    }

    @Override
    public KnowledgeSource get(UUID id){
        log.debug("Getting knowledge source by id={}", id);
        return knowledgeRepository.get(id).orElseThrow(() -> {
            log.warn("Knowledge source not found, id={}", id);
            return new KnowledgeNotFoundException(id);
        });
    }

    @Override
    public Page<KnowledgeSource> getAll(Pageable pageable){
        log.debug("Getting all knowledge sources, page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return knowledgeRepository.getAll(pageable);
    }
}
