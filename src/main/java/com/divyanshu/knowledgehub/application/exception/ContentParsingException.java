package com.divyanshu.knowledgehub.application.exception;

public class ContentParsingException extends ApplicationException {
    public ContentParsingException(String message) {
        super(message);
    }

    public ContentParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
