package com.cho.songstagram.service;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public void save(Posts posts){
        postsRepository.save(posts);
    }

    @Transactional
    public void delete(Posts posts){
        postsRepository.delete(posts);
    }

    public Optional<Posts> findById(Long id){
        return postsRepository.findById(id);
    }
    public  Page<Posts> findAll(Pageable pageable){
        return postsRepository.findAll(pageable);
    }
}
