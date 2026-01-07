package com.divyanshu.knowledgehub.domain.exception;

public class DomainException extends RuntimeException {
    protected DomainException(String message){
        super(message);
    }
}
