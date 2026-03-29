package com.divyanshu.knowledgehub.infrastructure.model;

import java.util.Objects;

public record ContentResource(byte[] content, String contentType) {
    public ContentResource(byte[] content,
                           String contentType) {

        this.content = Objects.requireNonNull(content);
        this.contentType = contentType;
    }

}
