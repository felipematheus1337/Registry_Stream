package com.registry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {

    @Bean
    public S3Client s3Client() {

        return S3Client.create();

    }
}
