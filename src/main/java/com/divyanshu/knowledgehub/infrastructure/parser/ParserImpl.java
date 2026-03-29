package com.divyanshu.knowledgehub.infrastructure.parser;

import com.divyanshu.knowledgehub.application.exception.ContentParsingException;
import com.divyanshu.knowledgehub.infrastructure.model.ContentResource;
import com.divyanshu.knowledgehub.application.port.out.Parser;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
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

    public String parse(ContentResource resource) {
        if (resource.contentType().contains("html")) {
            return htmlParser(resource);
        } else if (resource.contentType().contains("pdf")) {
            return pdfParser(resource);
        }

        return new String(resource.content(), StandardCharsets.UTF_8);
    }

    public String htmlParser(ContentResource resource) {
        try {
            Document doc = Jsoup.parse(
                    new ByteArrayInputStream(resource.content()),
                    null,
                    ""
            );
            String text = doc.body().text();
            log.debug("HTML parsed, extracted {} characters", text.length());
            return text;
        } catch (IOException e) {
            throw new ContentParsingException("Failed to parse HTML content", e);
        }
    }

    public String pdfParser(ContentResource resource) {
        try (PDDocument pdf = Loader.loadPDF(resource.content())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdf);
            log.debug("PDF parsed, {} pages, extracted {} characters",
                    pdf.getNumberOfPages(), text.length());
            return text;
        } catch (IOException e) {
            throw new ContentParsingException("Failed to parse PDF content", e);
        }
    }
}
