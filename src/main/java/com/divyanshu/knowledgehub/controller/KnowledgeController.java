package com.divyanshu.knowledgehub.controller;

import com.divyanshu.knowledgehub.application.AddKnowledgeUseCase;
import com.divyanshu.knowledgehub.domain.model.KnowledgeSource;
import com.divyanshu.knowledgehub.controller.request.AddKnowledgeRequest;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/knowledge")
public class KnowledgeController {

    private final AddKnowledgeUseCase addKnowledgeUseCase;

    public KnowledgeController(AddKnowledgeUseCase addKnowledgeUseCase) {
        this.addKnowledgeUseCase = addKnowledgeUseCase;
    }

    @PostMapping
    public KnowledgeSource add(@RequestBody AddKnowledgeRequest request) {

        KnowledgeSource source = new KnowledgeSource(
                UUID.randomUUID(),
                request.getType(),
                request.getContent(),
                request.getSourceUrl(),
                Instant.now()
        );

        return addKnowledgeUseCase.add(source);
    }
}