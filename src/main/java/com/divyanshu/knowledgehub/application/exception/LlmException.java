package com.divyanshu.knowledgehub.application.exception;

public class LlmException extends ApplicationException {
    public LlmException(String message) {
        super(message);
    }

    public LlmException(String message, Throwable cause) {
        super(message, cause);
    }
}
