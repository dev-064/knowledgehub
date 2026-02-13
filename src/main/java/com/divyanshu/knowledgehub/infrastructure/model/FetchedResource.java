package com.divyanshu.knowledgehub.infrastructure.model;

import java.util.Map;
import java.util.Objects;

public class FetchedResource {
    private final byte[] content;
    private final String contentType;
    private final Map<String,String> headers;
    private final String sourceUrl;

    public FetchedResource(byte[] content,
                           String contentType,
                           Map<String, String> headers,
                           String sourceUrl) {

        this.content = Objects.requireNonNull(content);
        this.contentType = contentType;
        this.headers = headers;
        this.sourceUrl = sourceUrl;
    }

    public byte[] getContent() {
        return content;
    }

    public String getContentType() {
        return contentType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

}
