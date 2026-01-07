package com.divyanshu.knowledgehub.controller.request;

import com.divyanshu.knowledgehub.domain.model.SourceType;

public class AddKnowledgeRequest {

    private SourceType type;
    private String content;
    private String sourceUrl;

    public SourceType getType() {
        return type;
    }

    public void setType(SourceType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}