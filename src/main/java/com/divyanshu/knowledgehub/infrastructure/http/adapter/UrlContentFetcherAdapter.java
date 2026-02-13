package com.divyanshu.knowledgehub.infrastructure.http.adapter;

import com.divyanshu.knowledgehub.application.port.out.UrlContentFetcher;
import com.divyanshu.knowledgehub.infrastructure.model.FetchedResource;
import org.hibernate.mapping.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UrlContentFetcherAdapter implements UrlContentFetcher {

    private final Logger log = LoggerFactory.getLogger(UrlContentFetcher.class);

    private final RestTemplate httpRestTemplate;

    public UrlContentFetcherAdapter(@Qualifier("httpRestTemplate") RestTemplate httpRestTemplate) {
        this.httpRestTemplate = httpRestTemplate;
    }

    public FetchedResource fetchUrlContent(String sourceUrl){
        String f =  httpRestTemplate.getForObject(sourceUrl,String.class);
        log.info("Fetched resource f= {}", f.toString());
        return new FetchedResource(new byte[],"xyz",new Map(),);
    }
}
