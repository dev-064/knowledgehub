package com.divyanshu.knowledgehub.controller;

import com.divyanshu.knowledgehub.application.AddKnowledgeUseCase;
import com.divyanshu.knowledgehub.application.GetKnowledgeUseCase;
import com.divyanshu.knowledgehub.domain.model.KnowledgeSource;
import com.divyanshu.knowledgehub.controller.request.AddKnowledgeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/knowledge")
public class KnowledgeController {

    private final AddKnowledgeUseCase addKnowledgeUseCase;
    private final GetKnowledgeUseCase getKnowledgeUseCase;

    public KnowledgeController(AddKnowledgeUseCase addKnowledgeUseCase, GetKnowledgeUseCase getKnowledgeUseCase) {
        this.addKnowledgeUseCase = addKnowledgeUseCase;
        this.getKnowledgeUseCase = getKnowledgeUseCase;
    }

    @PostMapping
    public KnowledgeSource add(@RequestBody AddKnowledgeRequest request) {

        KnowledgeSource source = new KnowledgeSource(
                UUID.randomUUID(),
                request.getType(),
                "request.getContent()",
                request.getSourceUrl(),
                Instant.now()
        );

        return addKnowledgeUseCase.add(source);
    }

    @GetMapping("/{id}")
    public KnowledgeSource getById(@PathVariable UUID id) {
        return getKnowledgeUseCase.get(id);
    }

    @GetMapping
    public Page<KnowledgeSource> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page,size);
        return getKnowledgeUseCase.getAll(pageable);
    }

}