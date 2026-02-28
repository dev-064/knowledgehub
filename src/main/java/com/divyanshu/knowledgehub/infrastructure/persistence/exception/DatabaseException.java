package com.divyanshu.knowledgehub.infrastructure.persistence.exception;

public class DatabaseException extends PersistenceException {
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
