package com.divyanshu.knowledgehub.infrastructure.http.adapter;

import com.divyanshu.knowledgehub.application.exception.LlmException;
import com.divyanshu.knowledgehub.application.port.out.LlmProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class OllamaLlmAdapter implements LlmProvider {

    private static final Logger log = LoggerFactory.getLogger(OllamaLlmAdapter.class);

    private final RestTemplate ollamaLlmRestTemplate;
    private final String ollamaBaseUrl;
    private final String model;

    public OllamaLlmAdapter(
            @Qualifier("ollamaLlmRestTemplate") RestTemplate ollamaLlmRestTemplate,
            @Value("${ollama.base-url}") String ollamaBaseUrl,
            @Value("${ollama.llm.model}") String model
    ) {
        this.ollamaLlmRestTemplate = ollamaLlmRestTemplate;
        this.ollamaBaseUrl = ollamaBaseUrl;
        this.model = model;
    }

    @Override
    public String answer(String query, List<String> chunks) {
        if (chunks == null || chunks.isEmpty()) {
            throw new LlmException("No context chunks provided to answer the query");
        }

        log.info("Requesting LLM answer from Ollama model '{}' with {} chunks", model, chunks.size());

        String url = ollamaBaseUrl + "/api/chat";
        String chunkContext = String.join("\n\n", chunks);

        List<Message> messages = List.of(
                new Message("system", "Use the following data to answer the user:\n\n" + chunkContext),
                new Message("user", query)
        );

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", messages,
                "stream", false
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<OllamaLlmResponse> response = ollamaLlmRestTemplate.exchange(
                    url, HttpMethod.POST, request, OllamaLlmResponse.class
            );

            OllamaLlmResponse body = response.getBody();
            if (body == null || body.message() == null || body.message().content() == null) {
                throw new LlmException("LLM service returned an empty response for model '" + model + "'");
            }

            log.info("Received LLM response from Ollama model '{}'", model);
            return body.message().content();

        } catch (ResourceAccessException e) {
            throw new LlmException("LLM service is unreachable at: " + url, e);
        } catch (RestClientException e) {
            throw new LlmException("LLM service request failed for model '" + model + "'", e);
        }
    }

    private record Message(String role, String content) {}

    private record OllamaLlmResponse(Message message) {}
}
