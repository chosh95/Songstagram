package com.cho.songstagram.service;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@Transactional
class PostsServiceTest {

    @Autowired PostsService postsService;
    @Autowired UsersService usersService;

    @Test
    public void 게시글_저장(){
        Users user1 = makeUser();
        usersService.save(user1);

        Posts posts = makePosts(user1);
        postsService.save(posts);

        assertEquals(posts,postsService.findById(posts.getId()).orElse(new Posts()));
    }

    @Test
    public void 게시글_삭제(){
        Users user1 = makeUser();
        usersService.save(user1);

        Posts posts = makePosts(user1);

        postsService.save(posts);
        assertNotNull(postsService.findById(posts.getId()).orElse(new Posts()));

        postsService.delete(posts);
        assertEquals(postsService.findById(posts.getId()), Optional.empty());
    }

    @Test
    public void 아이디로_게시글_찾기(){
        Users user1 = makeUser();
        usersService.save(user1);

        Posts posts = makePosts(user1);
        postsService.save(posts);

        assertEquals(posts,postsService.findById(posts.getId()).orElse(new Posts()));
    }

    @Test
    public void 작성된_모든_게시글_수(){
        Users user1 = makeUser();
        usersService.save(user1);

        Posts posts = makePosts(user1);
        postsService.save(posts);

        Posts posts2 = makePosts(user1);
        postsService.save(posts2);

        Posts posts3 = makePosts(user1);
        postsService.save(posts3);

        assertEquals(3,postsService.getPostsCount());
    }

    @Test
    public void 유저가_오늘_작성한_글의_수(){
        Users user1 = makeUser();
        usersService.save(user1);

        Posts posts = makePosts(user1);
        postsService.save(posts);

        assertEquals(1,postsService.getPostsCntByUser(user1));
    }

    @Test
    public void 유저가_작성한_총_게시글_수(){
        Users user1 = makeUser();
        usersService.save(user1);

        Posts posts = makePosts(user1);
        postsService.save(posts);

        Posts posts2= makePosts(user1);
        postsService.save(posts2);

        assertEquals(2,postsService.getPostsCntByUser(user1));
    }

    public Users makeUser(){
        return Users.builder()
                .email("abc@abc.com")
                .password("1234")
                .name("kim")
                .build();
    }

    public Posts makePosts(Users users){
        return Posts.builder()
                .content("글내용")
                .singer("가수")
                .songName("곡제목")
                .picture("사진 경로")
                .users(users)
                .build();
    }

}