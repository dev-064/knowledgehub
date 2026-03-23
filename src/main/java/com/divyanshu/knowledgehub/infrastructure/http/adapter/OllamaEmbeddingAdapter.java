package com.divyanshu.knowledgehub.infrastructure.http.adapter;

import com.divyanshu.knowledgehub.application.port.out.EmbeddingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class OllamaEmbeddingAdapter implements EmbeddingProvider {

    private static final Logger log = LoggerFactory.getLogger(OllamaEmbeddingAdapter.class);

    private final RestTemplate httpRestTemplate;
    private final String ollamaBaseUrl;
    private final String model;

    public OllamaEmbeddingAdapter(
            @Qualifier("httpRestTemplate") RestTemplate httpRestTemplate,
            @Value("${ollama.base-url}") String ollamaBaseUrl,
            @Value("${ollama.embedding.model}") String model
    ) {
        this.httpRestTemplate = httpRestTemplate;
        this.ollamaBaseUrl = ollamaBaseUrl;
        this.model = model;
    }

    @Override
    public List<float[]> generateEmbeddings(List<String> texts) {
        log.info("Requesting embeddings for {} texts from Ollama model '{}'", texts.size(), model);

        String url = ollamaBaseUrl + "/api/embed";

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "input", texts
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<OllamaEmbedResponse> response = httpRestTemplate.exchange(
                url, HttpMethod.POST, request, OllamaEmbedResponse.class
        );

        OllamaEmbedResponse body = response.getBody();
        if (body == null || body.embeddings() == null) {
            throw new RuntimeException("Empty response from Ollama embedding API");
        }

        log.info("Received {} embeddings from Ollama", body.embeddings().size());
        return body.embeddings();
    }

    private record OllamaEmbedResponse(List<float[]> embeddings) {}
}
