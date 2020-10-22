package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.CommentDto;
import com.cho.songstagram.makeComponent.MakeComponent;
import com.cho.songstagram.service.CommentsService;
import com.cho.songstagram.service.PostsService;
import com.cho.songstagram.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired UsersService usersService;
    @Autowired PostsService postsService;
    @Autowired CommentsService commentsService;
    @Autowired MockMvc mockMvc;
    @Autowired MakeComponent makeComponent;

    @Test
    public void 댓글_등록_테스트() throws Exception {
        Users users = makeComponent.makeUsers();
        usersService.save(users);

        Posts posts = makeComponent.makePosts(users);
        postsService.save(posts);

        mockMvc.perform(post("/comment/write/" + posts.getId().toString() + "&" + users.getId().toString())
                .sessionAttr("loginUser",users)
                .param("postId",posts.getId().toString())
                .param("userId",users.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/read/" + posts.getId().toString()));

        assertNotNull(commentsService.findCommentsByPosts(posts));
    }

    @Test
    public void 댓글_삭제_테스트() throws Exception {
        Users users = makeComponent.makeUsers();
        usersService.save(users);

        Posts posts = makeComponent.makePosts(users);
        postsService.save(posts);

        CommentDto commentDto = CommentDto.builder()
                .userId(users.getId())
                .comment("comment")
                .createdDate(LocalDateTime.now().toString())
                .build();

        commentsService.save(posts.getId(),users.getId(), commentDto);
        Set<Comments> commentsAndUsersByPosts = commentsService.findCommentsAndUsersByPosts(posts);
        Iterator<Comments> iterator = commentsAndUsersByPosts.iterator();

        mockMvc.perform(
                get("/comment/delete/" + iterator.next().getId().toString() + "&" + posts.getId().toString())
                        .sessionAttr("loginUser",users))
                .andExpect(redirectedUrl("/post/read/" + posts.getId().toString()));

        assertTrue(commentsService.findCommentsByPosts(posts).isEmpty());
    }

    @Test
    public void 사용자_권한없을때() throws Exception {
        Users users = makeComponent.makeUsers();
        usersService.save(users);

        mockMvc.perform(
                get("/comment/noAuthority")
                        .sessionAttr("loginUser",users))
                .andExpect(status().isOk())
                .andExpect(view().name("comment/noAuthority"));
    }
}