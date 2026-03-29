package com.divyanshu.knowledgehub.application.port.out;

import com.divyanshu.knowledgehub.infrastructure.model.ContentResource;

public interface UrlContentFetcher {
    ContentResource fetchUrlContent(String sourceUrl);
}
