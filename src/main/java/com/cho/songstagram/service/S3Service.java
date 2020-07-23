package com.cho.songstagram.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@Service
@NoArgsConstructor
public class S3Service {
    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public String postUpload(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString();
        s3Client.putObject(new PutObjectRequest(bucket+"/upload/post", fileName, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(bucket+"/upload/post", fileName).toString();
    }

    public String userUpload(MultipartFile file) throws IOException {
        if(file.isEmpty()) return "https://elasticbeanstalk-us-east-2-089357406904.s3.us-east-2.amazonaws.com/upload/profile/profile.png";
        String fileName = UUID.randomUUID().toString();
        s3Client.putObject(new PutObjectRequest(bucket+"/upload/profile", fileName, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(bucket+"/upload/profile", fileName).toString();
    }


    //delete 기능은 aws s3에 대한 접근 권한 문제로, 가능은 하지만 보안 문제상 기능하지 않도록 한다.
    public void deletePost(String fileUrl){
        int i = fileUrl.indexOf("/upload/post");
        String fileName = fileUrl.substring(i + 13);
        s3Client.deleteObject(new DeleteObjectRequest(bucket+"/upload/post",fileName));
    }

    public void deleteUser(String fileUrl){
        int i = fileUrl.indexOf("/upload/profile");
        String fileName = fileUrl.substring(i + 16);
        s3Client.deleteObject(new DeleteObjectRequest(bucket+"/upload/profile",fileName));
    }
}

