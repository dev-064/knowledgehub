package com.divyanshu.knowledgehub.infrastructure.http.adapter;

import com.divyanshu.knowledgehub.application.exception.UrlFetchException;
import com.divyanshu.knowledgehub.application.port.out.UrlContentFetcher;
import com.divyanshu.knowledgehub.infrastructure.model.ContentResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class UrlContentFetcherAdapter implements UrlContentFetcher {

    private final Logger log = LoggerFactory.getLogger(UrlContentFetcherAdapter.class);

    private final RestTemplate httpRestTemplate;

    public UrlContentFetcherAdapter(@Qualifier("httpRestTemplate") RestTemplate httpRestTemplate) {
        this.httpRestTemplate = httpRestTemplate;
    }

    public ContentResource fetchUrlContent(String sourceUrl) {
        try {
            ResponseEntity<byte[]> response = httpRestTemplate.exchange(
                    sourceUrl,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    byte[].class
            );
            byte[] body = response.getBody();
            if (body == null || body.length == 0) {
                throw new UrlFetchException("No content returned from URL: " + sourceUrl);
            }
            String contentType = response.getHeaders().getContentType() != null
                    ? response.getHeaders().getContentType().toString() : "application/octet-stream";
            log.debug("Fetched {} bytes of type '{}' from {}", body.length, contentType, sourceUrl);
            return new ContentResource(body, contentType);
        } catch (HttpClientErrorException e) {
            throw new UrlFetchException(
                    "URL returned " + e.getStatusCode().value() + " for: " + sourceUrl, e);
        } catch (HttpServerErrorException e) {
            throw new UrlFetchException(
                    "Remote server error " + e.getStatusCode().value() + " for: " + sourceUrl, e);
        } catch (ResourceAccessException e) {
            throw new UrlFetchException("Could not reach URL: " + sourceUrl, e);
        }
    }
}
