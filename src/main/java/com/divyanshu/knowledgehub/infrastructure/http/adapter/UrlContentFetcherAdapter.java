package com.divyanshu.knowledgehub.infrastructure.http.adapter;

import com.divyanshu.knowledgehub.application.port.out.UrlContentFetcher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UrlContentFetcherAdapter implements UrlContentFetcher {

    private final RestTemplate httpRestTemplate;

    public UrlContentFetcherAdapter(@Qualifier("httpRestTemplate") RestTemplate httpRestTemplate) {
        this.httpRestTemplate = httpRestTemplate;
    }

    public String fetchUrlContent(String sourceUrl){
        return httpRestTemplate.getForObject(sourceUrl,String.class);
    }
}
