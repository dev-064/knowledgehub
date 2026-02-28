package com.divyanshu.knowledgehub.infrastructure.persistence.exception;

public class DuplicateEntityException extends PersistenceException {
    public DuplicateEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
