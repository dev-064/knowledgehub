package com.divyanshu.knowledgehub.application.exception;

public class InvalidCredentialsException extends ApplicationException {
    public InvalidCredentialsException() {
        super("Invalid password");
    }
}
