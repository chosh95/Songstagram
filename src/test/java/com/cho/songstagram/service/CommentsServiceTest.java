package com.cho.songstagram.service;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.CommentDto;
import com.cho.songstagram.makeComponent.MakeComponent;
import com.cho.songstagram.repository.CommentsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class CommentsServiceTest {

    @Autowired UsersService usersService;
    @Autowired PostsService postsService;
    @Autowired CommentsService commentsService;
    @Autowired CommentsRepository commentsRepository;
    @Autowired MakeComponent makeComponent;

    @Test
    public void 댓글_저장() {
        Users users = makeComponent.makeUsers();
        usersService.save(users);

        Posts posts = makeComponent.makePosts(users);
        postsService.save(posts);

        Comments comments = makeComponent.makeComments(users, posts);
        CommentDto commentDto = CommentDto.builder()
                .comment(comments.getContent())
                .build();

        commentsService.save(posts.getId(),users.getId(),commentDto);

        Comments comments1 = commentsService.findCommentsByPosts(posts).get(0);
        assertEquals(comments.getContent(),commentsRepository.findById(comments1.getId()).orElseGet(Comments::new).getContent());
        assertEquals(comments.getUsers(),commentsRepository.findById(comments1.getId()).orElseGet(Comments::new).getUsers());
        assertEquals(comments.getPosts(),commentsRepository.findById(comments1.getId()).orElseGet(Comments::new).getPosts());
    }

    @Test
    public void 댓글_삭제(){
        Users users = makeComponent.makeUsers();
        usersService.save(users);

        Posts posts = makeComponent.makePosts(users);
        postsService.save(posts);

        CommentDto commentDto = CommentDto.builder()
                .comment("comment")
                .build();

        commentsService.save(posts.getId(),users.getId(),commentDto);
        List<Comments> commentsByPosts = commentsService.findCommentsByPosts(posts);
        Comments comments = commentsByPosts.get(0);

        commentsService.delete(comments.getId());
        assertTrue(commentsService.findCommentsByPosts(posts).isEmpty());
    }

}