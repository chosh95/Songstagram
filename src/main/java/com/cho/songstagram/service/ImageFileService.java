package com.cho.songstagram.service;

import com.cho.songstagram.domain.ImageFile;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.repository.ImageFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageFileService {

    private final ImageFileRepository imageFileRepository;

    public ImageFile storeFile(MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try{
            if(fileName.contains("..")){
                throw new RuntimeException("파일명이 잘못되었습니다.");
            }

            ImageFile imageFile = new ImageFile(fileName, file.getContentType(), file.getBytes());
            return imageFileRepository.save(imageFile);
        } catch(IOException ex){
            throw new RuntimeException("파일을 저장할 수 없습니다.");
        }
    }

    public ImageFile getFile(String fileId) throws FileNotFoundException {
        return imageFileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("file not found"));
    }
}
