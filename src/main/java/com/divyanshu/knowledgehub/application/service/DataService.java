package com.divyanshu.knowledgehub.application.service;


import com.divyanshu.knowledgehub.application.event.DocumentSavedEvent;
import com.divyanshu.knowledgehub.application.exception.ContentParsingException;
import com.divyanshu.knowledgehub.application.exception.DocumentAlreadyExistsException;
import com.divyanshu.knowledgehub.application.exception.WorkspaceNotFoundException;
import com.divyanshu.knowledgehub.application.port.out.DataRepository;
import com.divyanshu.knowledgehub.application.port.out.UrlContentFetcher;
import com.divyanshu.knowledgehub.domain.exception.InvalidDataException;
import com.divyanshu.knowledgehub.domain.model.Document;
import com.divyanshu.knowledgehub.domain.model.DocumentType;
import com.divyanshu.knowledgehub.infrastructure.model.ContentResource;
import com.divyanshu.knowledgehub.infrastructure.persistence.exception.DuplicateEntityException;
import com.divyanshu.knowledgehub.infrastructure.persistence.exception.EntityNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;

@Service
public class DataService {

    private final DataRepository dataRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final UrlContentFetcher urlContentFetcher;

    public DataService(
            DataRepository dataRepository,
            ApplicationEventPublisher eventPublisher,
            UrlContentFetcher urlContentFetcher
    ) {
        this.dataRepository = dataRepository;
        this.eventPublisher = eventPublisher;
        this.urlContentFetcher = urlContentFetcher;
    }

    public Document saveData(Object content, Document doc, MultipartFile file) {
        DocumentType docType = Objects.requireNonNull(doc.getDocumentType());
        ContentResource contentToBeProcessed;
        if (docType == DocumentType.LINK) {
            contentToBeProcessed = urlContentFetcher.fetchUrlContent(doc.getSourceUrl());
        } else if (docType == DocumentType.PDF) {
            if (file == null || file.isEmpty()) {
                throw new InvalidDataException("A PDF file must be provided for document type PDF");
            }
            try {
                contentToBeProcessed = new ContentResource(file.getBytes(), "pdf");
            } catch (IOException e) {
                throw new ContentParsingException("Failed to read uploaded PDF file", e);
            }
        } else if (docType == DocumentType.TEXT) {
            contentToBeProcessed = new ContentResource(((String) content).getBytes(StandardCharsets.UTF_8), "text");
        } else {
            throw new InvalidDataException("Unsupported document type: " + docType);
        }

        String contentHash = hashContent(contentToBeProcessed);
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

        try {
            Document savedDoc = dataRepository.save(docToBeSaved);
            eventPublisher.publishEvent(new DocumentSavedEvent(savedDoc.getId(), contentToBeProcessed));
            return savedDoc;
        } catch (DuplicateEntityException e) {
            throw new DocumentAlreadyExistsException(
                    "A document with identical content already exists in workspace: " + doc.getWorkspaceId());
        } catch (EntityNotFoundException e) {
            // Workspace FK violation — surface as a clear workspace-not-found error
            throw new WorkspaceNotFoundException(doc.getWorkspaceId());
        }
    }

    private String hashContent(ContentResource resource) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(resource.content());
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
