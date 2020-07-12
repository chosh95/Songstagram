package com.cho.songstagram.service;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.repository.CommentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentsService {

    private final CommentsRepository commentsRepository;

    @Transactional
    public void save(Comments comments){
        commentsRepository.save(comments);
    }

}
