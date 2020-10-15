package com.cho.songstagram.service;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.makeComponent.MakeComponent;
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
    @Autowired MakeComponent makeComponent;

    @Test
    public void 게시글_저장(){
        Users user1 = makeComponent.makeUsers();
        usersService.save(user1);

        Posts posts = makeComponent.makePosts(user1);
        postsService.save(posts);

        assertEquals(posts,postsService.findById(posts.getId()).orElseGet(Posts::new));
    }

    @Test
    public void 게시글_삭제(){
        Users user1 = makeComponent.makeUsers();
        usersService.save(user1);

        Posts posts = makeComponent.makePosts(user1);

        postsService.save(posts);
        assertNotNull(postsService.findById(posts.getId()).orElseGet(Posts::new));

        postsService.delete(posts);
        assertEquals(postsService.findById(posts.getId()), Optional.empty());
    }

    @Test
    public void 아이디로_게시글_찾기(){
        Users user1 = makeComponent.makeUsers();
        usersService.save(user1);

        Posts posts = makeComponent.makePosts(user1);
        postsService.save(posts);

        assertEquals(posts,postsService.findById(posts.getId()).orElseGet(Posts::new));
    }

    @Test
    public void 작성된_모든_게시글_수(){
        Users user1 = makeComponent.makeUsers();
        usersService.save(user1);

        Posts posts = makeComponent.makePosts(user1);
        postsService.save(posts);

        Posts posts2 = makeComponent.makePosts(user1);
        postsService.save(posts2);

        Posts posts3 = makeComponent.makePosts(user1);
        postsService.save(posts3);

        assertEquals(3,postsService.getPostsCount());
    }

    @Test
    public void 유저가_오늘_작성한_글의_수(){
        Users user1 = makeComponent.makeUsers();
        usersService.save(user1);

        Posts posts = makeComponent.makePosts(user1);
        postsService.save(posts);

        assertEquals(1,postsService.getPostsCntByUser(user1));
    }

    @Test
    public void 유저가_작성한_총_게시글_수(){
        Users user1 = makeComponent.makeUsers();
        usersService.save(user1);

        Posts posts = makeComponent.makePosts(user1);
        postsService.save(posts);

        Posts posts2= makeComponent.makePosts(user1);
        postsService.save(posts2);

        assertEquals(2,postsService.getPostsCntByUser(user1));
    }

}