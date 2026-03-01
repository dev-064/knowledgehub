package com.divyanshu.knowledgehub.application.exception;

import java.util.UUID;

public class WorkspaceNotFoundException extends ApplicationException {
    public WorkspaceNotFoundException(UUID id) {
        super("Workspace not found with id: " + id);
    }
}
