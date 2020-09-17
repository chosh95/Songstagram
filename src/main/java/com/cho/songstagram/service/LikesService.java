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
        Optional<Likes> byPostsAndUsers = likesRepository.findByPostsAndUsers(likes.getPosts(), likes.getUsers());
        if(byPostsAndUsers.isPresent()) throw new IllegalStateException("이미 좋아요를 눌렀습니다.");
        likesRepository.save(likes);
    }

    @Transactional
    public void delete(Likes likes) {likesRepository.delete(likes);}

    // 게시글과 유저 정보로 좋아요 눌렀는지 확인
    public Optional<Likes> findByPostsAndUsers(Posts posts, Users users){
        return likesRepository.findByPostsAndUsers(posts, users);
    }

    // 게시글에 좋아요 누른 user id 목록 반환
    public List<Long> findLikeUserIdList(Posts posts){
        List<Likes> byPosts = likesRepository.findByPosts(posts);
        List<Long> likesUserIdList = new ArrayList<>();
        for (Likes likes : byPosts) {
            likesUserIdList.add(likes.getUsers().getId());
        }
        return likesUserIdList;
    }
}
