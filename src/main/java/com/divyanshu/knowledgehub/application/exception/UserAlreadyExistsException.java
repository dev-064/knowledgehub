package com.divyanshu.knowledgehub.application.exception;

public class UserAlreadyExistsException extends ApplicationException {
    public UserAlreadyExistsException(String email) {
        super("User already exists with email: " + email);
    }
}
