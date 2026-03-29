package com.divyanshu.knowledgehub.application.port.out;

import com.divyanshu.knowledgehub.infrastructure.model.ContentResource;

public interface Parser {
    String parse(ContentResource resource);
}
