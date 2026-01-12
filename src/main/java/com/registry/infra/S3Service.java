package com.registry.infra;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    private final S3Client s3;

    public S3Service(S3Client s3) {
        this.s3 = s3;
    }

    public void putBytes(String bucket, String key, byte[] bytes, String contentType) {
        var req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        s3.putObject(req, RequestBody.fromBytes(bytes));
    }
}
