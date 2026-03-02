package com.divyanshu.knowledgehub.infrastructure.persistence.adapter;

import com.divyanshu.knowledgehub.application.port.out.DataRepository;
import com.divyanshu.knowledgehub.domain.model.Document;

import java.util.List;

public class DataRepositoryAdapter implements DataRepository {
    @Override
    public Document save(Document doc, List<String> chunks) {
        
    }
}
