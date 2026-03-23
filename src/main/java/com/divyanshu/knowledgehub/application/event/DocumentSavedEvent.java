package com.divyanshu.knowledgehub.application.event;

import java.util.UUID;

public record DocumentSavedEvent(UUID documentId) {}
