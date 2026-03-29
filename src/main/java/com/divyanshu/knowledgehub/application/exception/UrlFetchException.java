package com.divyanshu.knowledgehub.application.exception;

public class UrlFetchException extends ApplicationException {
    public UrlFetchException(String message) {
        super(message);
    }

    public UrlFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
