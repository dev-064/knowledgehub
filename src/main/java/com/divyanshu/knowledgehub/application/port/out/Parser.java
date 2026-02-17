package com.divyanshu.knowledgehub.application.port.out;

import com.divyanshu.knowledgehub.infrastructure.model.FetchedResource;

public interface Parser {
    String parse(FetchedResource resource);
}
