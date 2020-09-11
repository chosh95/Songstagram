package com.cho.songstagram.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @PostConstruct //객체 생성 후 자동으로 실행되는 메서드
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    //게시글 사진 업로드
    public String postUpload(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString(); // Universally Unique ID를 random으로 파일명 생성

        s3Client.putObject(new PutObjectRequest(bucket+"/upload/post", fileName, file.getInputStream(), null) //s3 버킷에 파일 저장
                .withCannedAcl(CannedAccessControlList.PublicRead)); //누구든 볼 수 있도록 public으로 read 권한 부여

        return s3Client.getUrl(bucket+"/upload/post", fileName).toString();
    }

    //프로필 이미지 업로드
    public String userUpload(MultipartFile file) throws IOException {
        if(file.isEmpty()) return "https://elasticbeanstalk-us-east-2-089357406904.s3.us-east-2.amazonaws.com/upload/profile/profile.png"; //사진 없으면 기본 프로필 이미지 지정
        
        String fileName = UUID.randomUUID().toString(); //uuid로 random 파일명 생성
        
        s3Client.putObject(new PutObjectRequest(bucket+"/upload/profile", fileName, file.getInputStream(), null) //s3 버킷에 파일 저장
                .withCannedAcl(CannedAccessControlList.PublicRead)); //누구든 볼 수 있도록 public으로 read 권한 부여
        
        return s3Client.getUrl(bucket+"/upload/profile", fileName).toString();
    }

    //게시글 이미지 삭제
    public void deletePost(String fileUrl){
        int i = fileUrl.indexOf("/upload/post"); // "https://elasticbeanstalk-us-east-2-089357406904.s3.us-east-2.amazonaws.com/upload/post/파일명" 에서 파일명 위치 가져오기
        String fileName = fileUrl.substring(i + 13); // UUID로 된 파일명만 뽑아오기
        s3Client.deleteObject(new DeleteObjectRequest(bucket+"/upload/post",fileName)); // s3에서 사진 삭제
    }

    public void deleteUser(String fileUrl){
        int i = fileUrl.indexOf("/upload/profile"); // "https://elasticbeanstalk-us-east-2-089357406904.s3.us-east-2.amazonaws.com/upload/profile/파일명" 에서 파일명 위치 가져오기
        String fileName = fileUrl.substring(i + 16); // UUID로 된 파일명만 뽑아오기
        s3Client.deleteObject(new DeleteObjectRequest(bucket+"/upload/profile",fileName)); //s3에서 프로필 사진 삭제
    }
}

