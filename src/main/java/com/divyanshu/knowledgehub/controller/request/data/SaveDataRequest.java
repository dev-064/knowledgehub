package com.divyanshu.knowledgehub.controller.request.data;

import com.divyanshu.knowledgehub.domain.model.DocumentType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SaveDataRequest {

    @NotBlank(message = "Title must not be blank")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private final String title;

    @NotNull(message = "Document type must not be null")
    private final DocumentType type;

    private final String content;

    private final String sourceUrl;

    @AssertTrue(message = "Content must not be blank for TEXT type")
    private boolean isContentValid() {
        return type != DocumentType.TEXT || (content != null && !content.isBlank());
    }

    @AssertTrue(message = "Source URL must not be blank for LINK type")
    private boolean isSourceUrlValid() {
        return type != DocumentType.LINK || (sourceUrl != null && !sourceUrl.isBlank());
    }

    @AssertTrue(message = "Source URL must be a valid URL")
    private boolean isSourceUrlFormatValid() {
        if (type != DocumentType.LINK || sourceUrl == null || sourceUrl.isBlank()) {
            return true;
        }
        return sourceUrl.startsWith("http://") || sourceUrl.startsWith("https://");
    }

    public SaveDataRequest(String title, DocumentType type, String content, String sourceUrl) {
        this.title = title;
        this.type = type;
        this.content = content;
        this.sourceUrl = sourceUrl;
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

    public String getSourceUrl() {
        return this.sourceUrl;
    }
}
