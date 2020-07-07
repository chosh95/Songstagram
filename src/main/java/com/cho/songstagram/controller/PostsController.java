package com.cho.songstagram.controller;

import com.cho.songstagram.domain.ImageFile;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.dto.ResponseFileDto;
import com.cho.songstagram.service.ImageFileService;
import com.cho.songstagram.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequiredArgsConstructor
public class PostsController {

    private final ImageFileService imageFileService;
    private final PostsService postsService;

    @PostMapping("/posts/add")
    public void addPost(@RequestParam("file") MultipartFile file,
                        String content, String singer, String songName){
        Posts posts = new Posts();
        ImageFile imageFile = imageFileService.storeFile(file);
        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(imageFile.getId())
                .toUriString();
        ResponseFileDto responseFileDto = new ResponseFileDto();
        responseFileDto.setFileName(imageFile.getFileName());
        responseFileDto.setFileUri(fileUri);
        responseFileDto.setFileSize(file.getSize());
        responseFileDto.setFileType(file.getContentType());
    }
}
