package com.divyanshu.knowledgehub.application.service;

import com.divyanshu.knowledgehub.domain.model.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SearchService {

    public List<Document> search(String query, UUID worskpaceID) {
        //generate embeddings of query, call the datarepository with the embeddings, then find the relevant documents
        return List.of();
    }
}
