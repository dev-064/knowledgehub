package com.divyanshu.knowledgehub.infrastructure.http.util;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class RetryHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final int MAX_ATTEMPTS = 3;
    private static final long BACKOFF_MS = 500;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        IOException lastException = null;
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                ClientHttpResponse response = execution.execute(request,body);
                if(response.getStatusCode().is5xxServerError() && attempt < MAX_ATTEMPTS) {
                    response.close();
                    sleep(BACKOFF_MS * attempt);
                    continue;
                }
                return response;
            } catch (IOException e) {
                lastException = e;
                if (attempt < MAX_ATTEMPTS) {
                    sleep(BACKOFF_MS * attempt);
                } else {
                    throw e;
                }
            }
        }
        throw lastException != null ? lastException : new IOException("Request failed after " + MAX_ATTEMPTS + " attempts");
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();;
            throw new RuntimeException("Interrupted during retry backoff", e);
        }
    }
}
