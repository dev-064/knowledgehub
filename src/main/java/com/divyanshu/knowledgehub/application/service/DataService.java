package com.divyanshu.knowledgehub.application.service;


import com.divyanshu.knowledgehub.application.exception.UserNotFoundException;
import com.divyanshu.knowledgehub.application.exception.WorkspaceNotFoundException;
import com.divyanshu.knowledgehub.application.port.out.DataRepository;
import com.divyanshu.knowledgehub.application.port.out.UserRepository;
import com.divyanshu.knowledgehub.application.port.out.WorkspaceRepository;
import com.divyanshu.knowledgehub.domain.model.Document;
import com.divyanshu.knowledgehub.domain.model.User;
import com.divyanshu.knowledgehub.domain.model.Workspace;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Service
public class DataService {

    private final DataRepository dataRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final ChunkingService chunkingService;

    public DataService(DataRepository dataRepository, WorkspaceRepository workspaceRepository, UserRepository userRepository, ChunkingService chunkingService) {
        this.dataRepository = dataRepository;
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
        this.chunkingService = chunkingService;
    }

    public Document saveData( String content, Document doc) {
        Workspace workspace = workspaceRepository.get(doc.getWorkspaceId()).orElseThrow(() -> new WorkspaceNotFoundException(doc.getWorkspaceId()));
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
        return dataRepository.save(docToBeSaved, chunks);
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
