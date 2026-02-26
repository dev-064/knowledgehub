package com.divyanshu.knowledgehub.application.exception;

public class UserAlreadyExistsException extends ApplicationException {
    public UserAlreadyExistsException(String email) {
        super("User with email '" + email + "' already exists");
    }
}
