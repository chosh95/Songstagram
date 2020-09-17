package com.cho.songstagram.service;

import com.cho.songstagram.domain.Likes;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LikesServiceTest {

    @Autowired LikesService likesService;
    @Autowired PostsService postsService;
    @Autowired UsersService usersService;

    @Test
    public void 좋아요_저장(){
        Users users = makeUser("abc@abc.com");
        usersService.save(users);

        Posts posts = makePosts(users);
        postsService.save(posts);

        Likes likes = makeLikes(posts, users);
        likesService.save(likes);

        assertEquals(likes,likesService.findByPostsAndUsers(posts,users).orElse(new Likes()));
    }

    @Test
    public void 좋아요_중복체크(){
        Users users = makeUser();
        usersService.save(users);

        Posts posts = makePosts(users);
        postsService.save(posts);

        Likes likes = makeLikes(posts, users);
        likesService.save(likes);

        Likes likes2 = makeLikes(posts, users);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> likesService.save(likes2),
                "중복 체크에 실패했습니다.");

        assertTrue(exception.getMessage().contains("이미 좋아요를 눌렀습니다."));
    }

    @Test
    public void 좋아요_삭제(){
        Users users = makeUser();
        usersService.save(users);

        Posts posts = makePosts(users);
        postsService.save(posts);

        Likes likes = makeLikes(posts, users);
        likesService.save(likes);

        likesService.delete(likes);
        assertEquals(likesService.findByPostsAndUsers(posts,users), Optional.empty());
    }

    @Test
    public void 게시글과_회원_정보로_좋아요_찾기(){
        Users users = makeUser();
        usersService.save(users);

        Posts posts = makePosts(users);
        postsService.save(posts);

        Likes likes = makeLikes(posts, users);
        likesService.save(likes);

        assertEquals(likes,likesService.findByPostsAndUsers(posts,users).orElse(new Likes()));
    }

    @Test
    public void 게시글_좋아요_누른_회원_아이디_목록_확인(){
        Users users = makeUser("abc@abc.com");
        usersService.save(users);

        Users users2 = makeUser("def@def.com");
        usersService.save(users2);

        Posts posts = makePosts(users);
        postsService.save(posts);

        Likes likes = makeLikes(posts, users);
        likesService.save(likes);
        Likes likes2 = makeLikes(posts, users2);
        likesService.save(likes2);

        List<Long> idList = new ArrayList<>();
        idList.add(users.getId());
        idList.add(users2.getId());

        assertEquals(idList,likesService.findLikeUserIdList(posts));
    }

    public Users makeUser(){
        return Users.builder()
                .email("abc@abc.com")
                .password("1234")
                .name("kim")
                .build();
    }

    public Users makeUser(String email){
        return Users.builder()
                .email(email)
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

    public Likes makeLikes(Posts posts, Users users){
        return Likes.builder()
                .posts(posts)
                .users(users)
                .build();
    }
}