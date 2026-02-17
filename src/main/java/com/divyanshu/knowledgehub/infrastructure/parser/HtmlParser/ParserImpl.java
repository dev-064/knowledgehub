package com.divyanshu.knowledgehub.infrastructure.parser.HtmlParser;

import com.divyanshu.knowledgehub.infrastructure.model.FetchedResource;
import com.divyanshu.knowledgehub.application.port.out.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ParserImpl implements Parser {

    private static final Logger log = LoggerFactory.getLogger(ParserImpl.class);

    public String parse(FetchedResource resource) {
        if (resource.getContentType().contains("text/html")) {
            log.info("Found the html content for sourceURL : {} ", resource.getSourceUrl());
            return htmlParser(resource);
        }

        return new String(resource.getContent(), StandardCharsets.UTF_8);
    }

    public String htmlParser(FetchedResource resource) {
        try {
            if (resource.getContentType() == null ||
                    !resource.getContentType().contains("text/html")) {
                throw new IllegalArgumentException("Not an HTML resource");
            }
            log.debug("Started parsing the content : {}", resource.getSourceUrl());
            Document document = Jsoup.parse(
                    new ByteArrayInputStream(resource.getContent()),
                    null,
                    resource.getSourceUrl()
            );
            log.info("parsed the content from : {}", resource.getSourceUrl());
            return document.text();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
