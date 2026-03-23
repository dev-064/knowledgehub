package com.divyanshu.knowledgehub.application.service;


import com.divyanshu.knowledgehub.application.event.DocumentSavedEvent;
import com.divyanshu.knowledgehub.application.port.out.DataRepository;
import com.divyanshu.knowledgehub.application.port.out.Parser;
import com.divyanshu.knowledgehub.application.port.out.UrlContentFetcher;
import com.divyanshu.knowledgehub.domain.model.Document;
import com.divyanshu.knowledgehub.domain.model.DocumentType;
import com.divyanshu.knowledgehub.infrastructure.model.FetchedResource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;

@Service
public class DataService {

    private final DataRepository dataRepository;
    private final ChunkingService chunkingService;
    private final ApplicationEventPublisher eventPublisher;
    private final UrlContentFetcher urlContentFetcher;
    private final Parser parser;

    public DataService(
            DataRepository dataRepository,
            ChunkingService chunkingService,
            ApplicationEventPublisher eventPublisher,
            UrlContentFetcher urlContentFetcher,
            Parser parser
    ) {
        this.dataRepository = dataRepository;
        this.chunkingService = chunkingService;
        this.eventPublisher = eventPublisher;
        this.urlContentFetcher = urlContentFetcher;
        this.parser = parser;
    }

    public Document saveData(String content, Document doc) {
        if (Objects.requireNonNull(doc.getDocumentType()) == DocumentType.LINK) {
            FetchedResource urlContent = urlContentFetcher.fetchUrlContent(doc.getSourceUrl());
            content = parser.parse(urlContent);
        }
        List<String> chunks = chunkingService.getChunks(content);
        String contentHash = hashContent(content);
        Document docToBeSaved = new Document(
                doc.getId(),
                doc.getWorkspaceId(),
                doc.getTitle(),
                doc.getSourceUrl(),
                doc.getDocumentType(),
                contentHash,
                doc.getUploadedReference(),
                doc.getStatus(),
                doc.getCreatedAt(),
                doc.getUpdatedAt(),
                List.of()
        );
        Document savedDoc = dataRepository.save(docToBeSaved, chunks);

        eventPublisher.publishEvent(new DocumentSavedEvent(savedDoc.getId()));

        return savedDoc;
    }

    private String hashContent(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
