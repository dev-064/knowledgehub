package com.divyanshu.knowledgehub.application.exception;

public class WorkspaceAlreadyExistsException extends ApplicationException {
    public WorkspaceAlreadyExistsException(String name) {
        super("Workspace already exists with name: " + name);
    }
}
