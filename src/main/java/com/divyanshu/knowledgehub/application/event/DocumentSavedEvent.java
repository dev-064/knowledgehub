package com.divyanshu.knowledgehub.application.event;

import com.divyanshu.knowledgehub.infrastructure.model.ContentResource;

import java.util.UUID;

public record DocumentSavedEvent(UUID documentId, ContentResource content) {}
