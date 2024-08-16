package com.beyond.teenkiri.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Component
public class UploadAwsFileService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final S3Client s3Client;
    private final CommonMethod commonMethod;

    @Autowired
    public UploadAwsFileService(S3Client s3Client, CommonMethod commonMethod) {
        this.s3Client = s3Client;
        this.commonMethod = commonMethod;
    }

    public String UploadAwsFileAndReturnPath(String fileName, byte[] fileData){

//        파일 확장자를 통한 업로드 여부 체크
        Boolean isUpload = commonMethod.fileSizeCheckFromByte(fileName,fileData);
        
        if (isUpload.equals(false)) {
            throw new IllegalArgumentException("파일의 크기가 너무 큽니다.");
        }
        
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        // S3에 파일 업로드
        PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileData));

        String s3FiltPath = s3Client.utilities()
                .getUrl(a->a.bucket(bucket).key(fileName)) // 해당 fileName으로 bucket 안에 있는 path를 찾아옴
                .toExternalForm();

        return s3FiltPath;
    }
}
