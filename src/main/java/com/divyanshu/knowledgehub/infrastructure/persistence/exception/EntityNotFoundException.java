package com.divyanshu.knowledgehub.infrastructure.persistence.exception;

public class EntityNotFoundException extends PersistenceException {
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
