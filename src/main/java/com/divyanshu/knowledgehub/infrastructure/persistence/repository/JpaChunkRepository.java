package com.divyanshu.knowledgehub.infrastructure.persistence.repository;

import com.divyanshu.knowledgehub.infrastructure.persistence.entity.ChunksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaChunkRepository extends JpaRepository<ChunksEntity, UUID> {

    List<ChunksEntity> findByDocumentIdOrderByChunkIndex(UUID documentId);

    @Query(value = """
            SELECT c.id, c.chunk_text, c.chunk_index,
                   d.id AS document_id, d.workspace_id, d.title, d.source_url,
                   d.type, d.content_hash, d.uploaded_reference, d.status,
                   d.created_at, d.updated_at,
                   1 - (c.embedding_vector <=> CAST(:queryVector AS vector)) AS similarity_score
            FROM chunks c
            INNER JOIN document d ON c.document_id = d.id
            WHERE d.workspace_id = :workspaceId
              AND c.embedding_vector IS NOT NULL
              AND 1 - (c.embedding_vector <=> CAST(:queryVector AS vector)) > :similarityThreshold
            ORDER BY c.embedding_vector <=> CAST(:queryVector AS vector)
            """, nativeQuery = true)
    List<Object[]> findByWorkspaceIdAboveSimilarityThreshold(@Param("queryVector") String queryVector, @Param("workspaceId") UUID workspaceId, @Param("similarityThreshold") double similarityThreshold);
}
