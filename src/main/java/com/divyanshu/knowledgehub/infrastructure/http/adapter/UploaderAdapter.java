package com.divyanshu.knowledgehub.infrastructure.http.adapter;

import com.divyanshu.knowledgehub.application.exception.StorageException;
import com.divyanshu.knowledgehub.application.port.out.Uploader;
import com.divyanshu.knowledgehub.infrastructure.http.util.CloudflareR2Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UploaderAdapter implements Uploader {

    private static final Logger log = LoggerFactory.getLogger(UploaderAdapter.class);

    @Value("${r2.public-access-url}")
    private String publicUrl;

    private final CloudflareR2Client cloudflareR2Client;
    private static final String BUCKET_NAME = "knowledgehub";

    public UploaderAdapter(CloudflareR2Client cloudflareR2Client) {
        this.cloudflareR2Client = cloudflareR2Client;
    }

    @Override
    public String upload(byte[] fileContent, String key) {
        try {
            log.debug("Uploading {} bytes to R2 bucket '{}' with key '{}'", fileContent.length, BUCKET_NAME, key);
            cloudflareR2Client.putObject(BUCKET_NAME, key, fileContent);
            String url = cloudflareR2Client.getUrl(publicUrl, key);
            log.debug("Successfully uploaded to R2: {}", url);
            return url;
        } catch (Exception e) {
            throw new StorageException("Failed to upload file with key '" + key + "' to storage", e);
        }
    }
}
