package com.divyanshu.knowledgehub.application.port.out;

import com.divyanshu.knowledgehub.domain.model.Document;

import java.util.List;

public interface DataRepository {
    Document save(Document doc, List<String> chunks);
}
