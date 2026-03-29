package com.divyanshu.knowledgehub.application.service;

import com.divyanshu.knowledgehub.application.event.DocumentSavedEvent;
import com.divyanshu.knowledgehub.application.exception.ContentParsingException;
import com.divyanshu.knowledgehub.application.exception.EmbeddingException;
import com.divyanshu.knowledgehub.application.exception.StorageException;
import com.divyanshu.knowledgehub.application.port.out.DataRepository;
import com.divyanshu.knowledgehub.application.port.out.EmbeddingProvider;
import com.divyanshu.knowledgehub.application.port.out.Parser;
import com.divyanshu.knowledgehub.application.port.out.Uploader;
import com.divyanshu.knowledgehub.domain.model.DocumentUploadStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentProcessorService {

    private static final Logger log = LoggerFactory.getLogger(DocumentProcessorService.class);

    private final EmbeddingProvider embeddingProvider;
    private final DataRepository dataRepository;
    private final Uploader uploader;
    private final Parser parser;
    private final ChunkingService chunkingService;

    public DocumentProcessorService(EmbeddingProvider embeddingProvider, DataRepository dataRepository, Uploader uploader, Parser parser, ChunkingService chunkingService) {
        this.embeddingProvider = embeddingProvider;
        this.dataRepository = dataRepository;
        this.uploader = uploader;
        this.parser = parser;
        this.chunkingService = chunkingService;
    }

    @Async("documentProcessor")
    @EventListener
    public void handleDocumentSaved(DocumentSavedEvent event) {
        UUID documentId = event.documentId();
        log.info("Starting document processing for document: {}", documentId);

        try {
            String parsedContent = parser.parse(event.content());
            List<String> chunks = chunkingService.getChunks(parsedContent);
            List<float[]> embeddings = embeddingProvider.generateEmbeddings(chunks);
            dataRepository.saveChunks(chunks, embeddings, documentId);

            String uploadedUrl = null;
            if (event.content().contentType().contains("pdf")) {
                uploadedUrl = uploader.upload(event.content().content(), documentId.toString());
            }

            dataRepository.updateDocumentStatus(documentId, DocumentUploadStatus.SUCCESS, uploadedUrl);
            log.info("Document processing completed for document: {}", documentId);
        } catch (ContentParsingException e) {
            log.error("Content parsing failed for document: {}", documentId, e);
            markFailed(documentId);
        } catch (EmbeddingException e) {
            log.error("Embedding service failed for document: {}", documentId, e);
            markFailed(documentId);
        } catch (StorageException e) {
            log.error("Storage upload failed for document: {}", documentId, e);
            markFailed(documentId);
        } catch (Exception e) {
            log.error("Unexpected error during document processing for: {}", documentId, e);
            markFailed(documentId);
        }
    }

    private void markFailed(UUID documentId) {
        try {
            dataRepository.updateDocumentStatus(documentId, DocumentUploadStatus.FAILED, null);
        } catch (Exception e) {
            log.error("Failed to mark document {} as FAILED — status may be stuck in PROCESSING", documentId, e);
        }
    }
}
