package com.divyanshu.knowledgehub.infrastructure.persistence.exception;

public class PersistenceException extends RuntimeException {
    protected PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
