package com.divyanshu.knowledgehub.controller;

import com.divyanshu.knowledgehub.application.service.DataService;
import com.divyanshu.knowledgehub.controller.request.data.SaveDataRequest;
import com.divyanshu.knowledgehub.controller.response.data.SaveDataResponse;
import com.divyanshu.knowledgehub.domain.model.Document;
import com.divyanshu.knowledgehub.domain.model.DocumentUploadStatus;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/data")
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping
    public SaveDataResponse saveData(@Valid @ModelAttribute SaveDataRequest request, @RequestParam UUID workspaceId, @RequestPart(required = false) MultipartFile file) {
        String content = request.getContent();
        Document doc = new Document(
                UUID.randomUUID(),
                workspaceId,
                request.getTitle(),
                request.getSourceUrl(),
                request.getDocumentType(),
                null,
                null,
                DocumentUploadStatus.PROCESSING,
                Instant.now(),
                Instant.now(),
                List.of()
        );
        Document savedDoc = dataService.saveData(content, doc, file);
        return new SaveDataResponse(
                savedDoc.getId(),
                savedDoc.getTitle(),
                savedDoc.getSourceUrl(),
                savedDoc.getDocumentType(),
                savedDoc.getUploadedReference(),
                savedDoc.getStatus(),
                savedDoc.getCreatedAt(),
                savedDoc.getUpdatedAt()
        );
    }
}
