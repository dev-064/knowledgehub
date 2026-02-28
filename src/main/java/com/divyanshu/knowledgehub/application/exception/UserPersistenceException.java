package com.divyanshu.knowledgehub.application.exception;

public class UserPersistenceException extends ApplicationException {
    public UserPersistenceException(String email) {
        super("Not able to save the user " + email);
    }
}
