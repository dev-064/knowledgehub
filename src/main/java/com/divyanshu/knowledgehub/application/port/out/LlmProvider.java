package com.divyanshu.knowledgehub.application.port.out;

import java.util.List;

public interface LlmProvider {
    String answer(String query, List<String> contextChunks);
}
