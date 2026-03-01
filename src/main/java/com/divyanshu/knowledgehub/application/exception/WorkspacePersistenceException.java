package com.divyanshu.knowledgehub.application.exception;

public class WorkspacePersistenceException extends ApplicationException {
    public WorkspacePersistenceException(String name) {
        super("Failed to save workspace: " + name);
    }
}
