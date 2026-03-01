package com.divyanshu.knowledgehub.controller.request.workspace;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateWorkspaceRequest {

    @NotBlank(message = "Workspace name must not be blank")
    @Size(max = 100, message = "Workspace name must not exceed 100 characters")
    private final String name;

    public CreateWorkspaceRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
