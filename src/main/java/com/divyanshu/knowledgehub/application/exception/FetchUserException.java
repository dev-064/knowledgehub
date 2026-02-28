package com.divyanshu.knowledgehub.application.exception;

import java.util.UUID;

public class FetchUserException extends ApplicationException {
    public FetchUserException(String email) {
        super("Not able to fetch the user " + email);
    }

    public FetchUserException(UUID id) {
        super("Not able to fetch the user " + id);
    }
}
