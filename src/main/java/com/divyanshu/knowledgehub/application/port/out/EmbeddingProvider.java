package com.divyanshu.knowledgehub.application.port.out;

import java.util.List;

public interface EmbeddingProvider {

    List<float[]> generateEmbeddings(List<String> texts);
}
