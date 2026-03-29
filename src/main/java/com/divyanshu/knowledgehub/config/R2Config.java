package com.divyanshu.knowledgehub.config;

import com.divyanshu.knowledgehub.infrastructure.http.util.CloudflareR2Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class R2Config {

    @Value("${r2.account-id}")
    private String accountId;

    @Value("${r2.access-key-id}")
    private String accessKey;

    @Value("${r2.access_key_secret}")
    private String secretKey;

    @Bean
    public CloudflareR2Client.S3Config s3Config() {
        return new CloudflareR2Client.S3Config(accountId, accessKey, secretKey);
    }
}
