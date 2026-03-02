package com.divyanshu.knowledgehub.application.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChunkingService {
    private static final int CHUNK_SIZE = 500;
    private static final int OVERLAP = 50;

    public List<String> getChunks(String content) {
        List<String> chunks = new ArrayList<>();
        int step = CHUNK_SIZE - OVERLAP;
        int start = 0;

        while (start < content.length()) {
            int end = Math.min(start + CHUNK_SIZE, content.length());
            chunks.add(content.substring(start, end));
            start += step;
        }

        return chunks;
    }
}
