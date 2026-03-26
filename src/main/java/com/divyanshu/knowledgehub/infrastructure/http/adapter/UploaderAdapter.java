package com.divyanshu.knowledgehub.infrastructure.http.adapter;

import com.divyanshu.knowledgehub.application.port.out.Uploader;
import com.divyanshu.knowledgehub.infrastructure.http.util.CloudflareR2Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class UploaderAdapter implements Uploader {

    @Value("${r2.account-id}")
    private String accountId;

    @Value("${r2.access-key-id}")
    private String accessKey;

    @Value("${r2.access_key_secret}")
    private String secretKey;

    @Value("${r2.public-url}")
    private String publicUrl;

    private final CloudflareR2Client cloudflareR2Client;
    private final String BUCKET_NAME = "knowledgehub";

    public UploaderAdapter(CloudflareR2Client cloudflareR2Client){
        this.cloudflareR2Client = cloudflareR2Client;
    }

    @Override
    public String upload(byte[] fileContent, String key) {
        cloudflareR2Client.putObject(BUCKET_NAME, key, fileContent);
        return cloudflareR2Client.getUrl(publicUrl, key);
    }
}
