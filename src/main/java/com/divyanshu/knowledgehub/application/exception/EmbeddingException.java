package com.divyanshu.knowledgehub.application.exception;

public class EmbeddingException extends ApplicationException {
    public EmbeddingException(String message) {
        super(message);
    }

    public EmbeddingException(String message, Throwable cause) {
        super(message, cause);
    }
}
