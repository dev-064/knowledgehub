package com.divyanshu.knowledgehub.application.port.out;

import org.springframework.web.multipart.MultipartFile;

public interface Uploader {
    String upload(byte[] fileContent, String key);
}
