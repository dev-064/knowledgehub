package com.divyanshu.knowledgehub.infrastructure.http.adapter;

import com.divyanshu.knowledgehub.application.port.out.UrlContentFetcher;
import com.divyanshu.knowledgehub.infrastructure.model.FetchedResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class UrlContentFetcherAdapter implements UrlContentFetcher {

    private final Logger log = LoggerFactory.getLogger(UrlContentFetcher.class);

    private final RestTemplate httpRestTemplate;

    public UrlContentFetcherAdapter(@Qualifier("httpRestTemplate") RestTemplate httpRestTemplate) {
        this.httpRestTemplate = httpRestTemplate;
    }

    public FetchedResource fetchUrlContent(String sourceUrl){
        ResponseEntity<byte[]> response = httpRestTemplate.exchange(
                sourceUrl,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                byte[].class
        );
        byte[] body = response.getBody();
        String contentType = response.getHeaders().getContentType() != null
                ? response.getHeaders().getContentType().toString() : "unknown";
        Map<String, String> headers = response.getHeaders().toSingleValueMap();
        return new FetchedResource(
                body,
                contentType,
                headers,
                sourceUrl
        );
    }
}
