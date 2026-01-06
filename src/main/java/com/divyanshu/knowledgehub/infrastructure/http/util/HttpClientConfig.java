package com.divyanshu.knowledgehub.infrastructure.http.util;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class HttpClientConfig {

    private static final int CONNECT_TIMEOUT_SECONDS = 10;
    private static final int SOCKET_TIMEOUT_SECONDS = 30;
    private static final int MAX_CONNECTION_TOTAL = 20;
    private static final int MAX_CONNECTION_PER_ROUTE = 5;

    @Bean
    public RestTemplate httpRestTemplate() {
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(CONNECT_TIMEOUT_SECONDS))
                .setSocketTimeout(Timeout.ofSeconds(SOCKET_TIMEOUT_SECONDS))
                .build();

        HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(
                        SSLConnectionSocketFactoryBuilder.create()
                                .setSslContext(SSLContexts.createSystemDefault())
                                .build()
                )
                .setDefaultConnectionConfig(connectionConfig)
                .setMaxConnTotal(MAX_CONNECTION_TOTAL)
                .setMaxConnPerRoute(MAX_CONNECTION_PER_ROUTE)
                .build();

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofSeconds(CONNECT_TIMEOUT_SECONDS))
                .setResponseTimeout(Timeout.ofSeconds(SOCKET_TIMEOUT_SECONDS))
                .build();

        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .evictExpiredConnections()
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        RestTemplate restTemplate = new RestTemplate((new BufferingClientHttpRequestFactory(requestFactory)));
        restTemplate.setInterceptors(List.of(new RetryHttpRequestInterceptor()));

        return restTemplate;
    }
}
