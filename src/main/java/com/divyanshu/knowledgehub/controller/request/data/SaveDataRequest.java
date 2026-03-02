package com.divyanshu.knowledgehub.controller.request.data;

import com.divyanshu.knowledgehub.domain.model.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SaveDataRequest {

    @NotBlank(message = "Title must not be blank")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private final String title;

    @NotNull(message = "Document type must not be null")
    private final DocumentType type;

    @NotBlank(message = "Content must not be blank")
    private final String content;

    public SaveDataRequest(String title, DocumentType type, String content) {
        this.title = title;
        this.type = type;
        this.content = content;
    }

    public String getTitle() {
        return this.title;
    }

    public DocumentType getDocumentType() {
        return this.type;
    }

    public String getContent() {
        return this.content;
    }
}
