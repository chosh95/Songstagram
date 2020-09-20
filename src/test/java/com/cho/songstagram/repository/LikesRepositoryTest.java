package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Likes;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.makeComponent.MakeComponent;
import com.cho.songstagram.service.LikesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LikesRepositoryTest {

    @Autowired
    LikesRepository likesRepository;
    @Autowired
    PostsRepository postsRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    MakeComponent makeComponent;
    @Test
    public void 게시글과_작성자로_좋아요_찾기(){
        Users users = makeComponent.makeUsers();
        usersRepository.save(users);

        Posts posts = makeComponent.makePosts(users);
        postsRepository.save(posts);

        Likes likes = makeComponent.makeLikes(posts, users);
        likesRepository.save(likes);

        assertEquals(likes,likesRepository.findByPostsAndUsers(posts,users).orElse(new Likes()));
    }

    @Test
    public void 좋아요_누른_게시글_목록(){
        Users users = makeComponent.makeUsers();
        usersRepository.save(users);
        Users users2 = makeComponent.makeUsers();
        usersRepository.save(users2);

        Posts posts = makeComponent.makePosts(users);
        postsRepository.save(posts);

        // 하나의 게시글에 두 명의 사람이 좋아요를 눌렀을 때
        Likes likes = makeComponent.makeLikes(posts, users);
        likesRepository.save(likes);
        Likes likes2 = makeComponent.makeLikes(posts, users2);
        likesRepository.save(likes2);

        List<Likes> likesList = new ArrayList<>();
        likesList.add(likes);
        likesList.add(likes2);

        // 게시글에 좋아요 누른 목록을 모두 반환해야한다.
        assertEquals(likesList,likesRepository.findByPosts(posts));
    }
}