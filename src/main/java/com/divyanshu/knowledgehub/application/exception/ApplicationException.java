package com.divyanshu.knowledgehub.application.exception;

public class ApplicationException extends RuntimeException {
    protected ApplicationException (String message) {
        super(message);
    }

    protected ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
