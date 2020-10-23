package com.cho.songstagram.service;

import com.cho.songstagram.domain.Likes;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.makeComponent.MakeComponent;
import com.cho.songstagram.repository.PostsRepository;
import com.cho.songstagram.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LikesServiceTest {

    @Autowired LikesService likesService;
    @Autowired PostsService postsService;
    @Autowired UsersService usersService;
    MakeComponent makeComponent = new MakeComponent();

    @Test
    public void 좋아요_저장() throws InterruptedException {
        Users users = makeComponent.makeUsers("abc@abc.com");
        usersService.save(users);

        Posts posts = makeComponent.makePosts(users);
        postsService.save(posts);

        Likes likes = makeComponent.makeLikes(posts, users);
        likesService.save(likes);

        assertEquals(likes,likesService.findByPostsAndUsers(posts,users).orElseGet(Likes::new));
    }

    @Test
    public void 좋아요_중복체크(){
        Users users = makeComponent.makeUsers();
        usersService.save(users);

        Posts posts = makeComponent.makePosts(users);
        postsService.save(posts);

        Likes likes = makeComponent.makeLikes(posts, users);
        likesService.save(likes);

        Likes likes2 = makeComponent.makeLikes(posts, users);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> likesService.save(likes2),
                "중복 체크에 실패했습니다.");

        assertTrue(exception.getMessage().contains("이미 좋아요를 눌렀습니다."));
    }

    @Test
    public void 좋아요_삭제(){
        Users users = makeComponent.makeUsers();
        usersService.save(users);

        Posts posts = makeComponent.makePosts(users);
        postsService.save(posts);

        Likes likes = makeComponent.makeLikes(posts, users);
        likesService.save(likes);

        likesService.delete(likes);

        assertEquals(likesService.findByPostsAndUsers(posts,users), Optional.empty());
    }

    @Test
    public void 게시글과_회원_정보로_좋아요_찾기(){
        Users users = makeComponent.makeUsers();
        usersService.save(users);

        Posts posts = makeComponent.makePosts(users);
        postsService.save(posts);

        Likes likes = makeComponent.makeLikes(posts, users);
        likesService.save(likes);

        assertEquals(likes,likesService.findByPostsAndUsers(posts,users).orElseGet(Likes::new));
    }

    @Test
    public void 게시글_좋아요_누른_회원_아이디_목록_확인(){
        Users users = makeComponent.makeUsers("abc@abc.com");
        usersService.save(users);

        Users users2 = makeComponent.makeUsers("def@def.com");
        usersService.save(users2);

        Posts posts = makeComponent.makePosts(users);
        postsService.save(posts);

        Likes likes = makeComponent.makeLikes(posts, users);
        likesService.save(likes);
        Likes likes2 = makeComponent.makeLikes(posts, users2);
        likesService.save(likes2);

        Set<Long> idList = new HashSet<>();
        idList.add(users.getId());
        idList.add(users2.getId());

        assertEquals(idList,likesService.findLikeUserIdList(posts));
    }
}