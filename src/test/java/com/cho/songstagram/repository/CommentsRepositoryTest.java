package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentsRepositoryTest {

    @Autowired UsersRepository usersRepository;
    @Autowired PostsRepository postsRepository;
    @Autowired CommentsRepository commentsRepository;

    @Test
    public void 게시글에_작성한_댓글목록_가져오기(){
        Users users = makeUser();
        usersRepository.save(users);

        Posts posts = makePosts(users);
        postsRepository.save(posts);

        Comments comments = makeComments(users,posts);
        commentsRepository.save(comments);

        Comments comments2 = makeComments(users,posts);
        commentsRepository.save(comments2);

        List<Comments> commentsList = new ArrayList<>();
        commentsList.add(comments);
        commentsList.add(comments2);

        assertEquals(commentsList,commentsRepository.findCommentsByPosts(posts));
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

    public Comments makeComments(Users users, Posts posts){
        return Comments.builder()
                .users(users)
                .posts(posts)
                .content("content")
                .build();
    }
}