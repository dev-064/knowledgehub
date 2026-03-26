package com.divyanshu.knowledgehub.application.service;

import com.divyanshu.knowledgehub.application.event.DocumentSavedEvent;
import com.divyanshu.knowledgehub.application.port.out.DataRepository;
import com.divyanshu.knowledgehub.application.port.out.EmbeddingProvider;
import com.divyanshu.knowledgehub.application.port.out.Uploader;
import com.divyanshu.knowledgehub.domain.model.Chunks;
import com.divyanshu.knowledgehub.domain.model.DocumentUploadStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmbeddingService {

    private static final Logger log = LoggerFactory.getLogger(EmbeddingService.class);

    private final EmbeddingProvider embeddingProvider;
    private final DataRepository dataRepository;
    private final Uploader uploader;

    public EmbeddingService(EmbeddingProvider embeddingProvider, DataRepository dataRepository, Uploader uploader) {
        this.embeddingProvider = embeddingProvider;
        this.dataRepository = dataRepository;
        this.uploader = uploader;
    }

    @Async("embeddingTaskExecutor")
    @EventListener
    public void handleDocumentSaved(DocumentSavedEvent event) {
        UUID documentId = event.documentId();
        log.info("Starting embedding generation for document: {}", documentId);

        try {
            uploader.upload(event.fileContent(), documentId.toString());

            List<Chunks> chunks = dataRepository.findChunksByDocumentId(documentId);
            if (chunks.isEmpty()) {
                log.warn("No chunks found for document: {}", documentId);
                dataRepository.updateDocumentStatus(documentId, DocumentUploadStatus.FAILED);
                return;
            }

            List<String> chunkTexts = chunks.stream()
                    .map(Chunks::getChunkText)
                    .toList();

            List<float[]> embeddings = embeddingProvider.generateEmbeddings(chunkTexts);

            if (embeddings.size() != chunks.size()) {
                log.error("Embedding count mismatch: expected {}, got {} for document: {}",
                        chunks.size(), embeddings.size(), documentId);
                dataRepository.updateDocumentStatus(documentId, DocumentUploadStatus.FAILED);
                return;
            }

            List<UUID> chunkIds = chunks.stream().map(Chunks::getId).toList();
            dataRepository.updateChunkEmbeddings(chunkIds, embeddings);

            dataRepository.updateDocumentStatus(documentId, DocumentUploadStatus.SUCCESS);
            log.info("Embedding generation completed for document: {}", documentId);

        } catch (Exception e) {
            log.error("Embedding generation failed for document: {}", documentId, e);
            dataRepository.updateDocumentStatus(documentId, DocumentUploadStatus.FAILED);
        }
    }
}
