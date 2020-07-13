package com.cho.songstagram.service;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.repository.CommentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;

    @Transactional
    public void save(Comments comments){
        commentsRepository.save(comments);
    }

    public List<Comments> findCommentsByPosts(Posts posts){
        return commentsRepository.findCommentsByPosts(posts);
    }
}
