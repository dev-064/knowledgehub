package com.divyanshu.knowledgehub.application.service;

import com.divyanshu.knowledgehub.application.AddKnowledgeUseCase;
import com.divyanshu.knowledgehub.application.exception.KnowledgePersistenceException;
import com.divyanshu.knowledgehub.application.port.out.KnowledgeRepository;
import com.divyanshu.knowledgehub.application.port.out.Parser;
import com.divyanshu.knowledgehub.application.port.out.UrlContentFetcher;
import com.divyanshu.knowledgehub.domain.model.KnowledgeSource;
import com.divyanshu.knowledgehub.infrastructure.model.FetchedResource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class AddKnowledgeService implements AddKnowledgeUseCase {

    private static final Logger log = LoggerFactory.getLogger(AddKnowledgeService.class);

    private final KnowledgeRepository knowledgeRepository;
    private final UrlContentFetcher urlContentFetcher;
    private final Parser parser;

    public AddKnowledgeService(KnowledgeRepository knowledgeRepository, UrlContentFetcher urlContentFetcher, Parser parser) {
        this.knowledgeRepository = knowledgeRepository;
        this.urlContentFetcher = urlContentFetcher;
        this.parser = parser;
    }

    @Override
    public KnowledgeSource add(KnowledgeSource source) {
        try{
            log.debug("Adding knowledge source, id={}, type={}", source.getId(), source.getType());
            KnowledgeSource processedSource = switch (source.getType()) {
                case TEXT -> source;
                case LINK -> {
                    log.debug("Fetching content from URL: {}", source.getSourceUrl());
                    FetchedResource content = urlContentFetcher.fetchUrlContent(source.getSourceUrl());
                    String parsedContent = parser.parse(content);

                    yield new KnowledgeSource(
                        source.getId(),
                        source.getType(),
                        parsedContent,
                        source.getSourceUrl(),
                        source.getCreatedAt()
                    );
                }
            };
            log.debug("Saving knowledge source, id={}", processedSource.getId());
            KnowledgeSource saved =  knowledgeRepository.save(processedSource);
            log.info("Successfully added knowledge source, id={}", saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("Failed to add knowledge source, id={}, type={}", source.getId(), source.getType());
            throw new KnowledgePersistenceException("Failed to persist source", e);
        }
    }
}
