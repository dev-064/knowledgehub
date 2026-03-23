package com.divyanshu.knowledgehub.controller;

import com.divyanshu.knowledgehub.application.service.DataService;
import com.divyanshu.knowledgehub.controller.request.data.SaveDataRequest;
import com.divyanshu.knowledgehub.controller.response.data.SaveDataResponse;
import com.divyanshu.knowledgehub.domain.model.Document;
import com.divyanshu.knowledgehub.domain.model.DocumentUploadStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    public SaveDataResponse saveData(@Valid @RequestBody SaveDataRequest request, @RequestParam UUID workspaceId) {
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
        Document savedDoc = dataService.saveData(content, doc);
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
