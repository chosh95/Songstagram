package com.cho.songstagram.service;

import com.cho.songstagram.domain.Likes;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikesService {

    private final LikesRepository likesRepository;

    @Transactional
    public void save(Likes likes){
        likesRepository.save(likes);
    }

    @Transactional
    public void delete(Likes likes) {likesRepository.delete(likes);}

    public Optional<Likes> findByPostsAndUsers(Posts posts, Users users){
        return likesRepository.findByPostsAndUsers(posts, users);
    }

    public List<Long> findLikeIdList(Posts posts){
        List<Likes> byPosts = likesRepository.findByPosts(posts);
        List<Long> likesIdList = new ArrayList<>();
        for (Likes likes : byPosts) {
            likesIdList.add(likes.getUsers().getId());
        }
        return likesIdList;
    }
}
