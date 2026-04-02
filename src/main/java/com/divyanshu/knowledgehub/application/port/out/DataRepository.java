package com.divyanshu.knowledgehub.application.port.out;

import com.divyanshu.knowledgehub.domain.model.Chunks;
import com.divyanshu.knowledgehub.domain.model.Document;
import com.divyanshu.knowledgehub.domain.model.DocumentUploadStatus;

import java.util.List;
import java.util.UUID;

public interface DataRepository {

    Document save(Document doc);

    List<Chunks> saveChunks(List<String> chunks, List<float[]> embeddings, UUID documentId);

    List<Chunks> findChunksByDocumentId(UUID documentId);

    void updateChunkEmbeddings(List<UUID> chunkIds, List<float[]> embeddings);

    void updateDocumentStatus(UUID documentId, DocumentUploadStatus status, String uploadedUrl);

    List<Document> findRelevantDocuments(float[] queryEmbeddings, UUID workspaceId);
}
