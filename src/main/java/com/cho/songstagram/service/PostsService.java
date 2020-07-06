package com.cho.songstagram.service;

import com.cho.songstagram.domain.ImageFile;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsService {

    private final PostsRepository postsRepository;

    public void addPost(String content, String singer, String songName, ImageFile imageFile, Users users){
        postsRepository.save(Posts.builder()
                .content(content)
                .singer(singer)
                .songName(songName)
                .imageFile(imageFile)
                .users(users)
                .build());
    }

}
