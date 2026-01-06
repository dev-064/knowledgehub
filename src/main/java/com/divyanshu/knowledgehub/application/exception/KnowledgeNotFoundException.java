package com.divyanshu.knowledgehub.application.exception;

import java.util.UUID;

public class KnowledgeNotFoundException extends ApplicationException{

    public KnowledgeNotFoundException(){
        super("Knowledge not found");
    }

    public KnowledgeNotFoundException(UUID id){
        super("knowledge with id" + id + "not found");
    }
}


