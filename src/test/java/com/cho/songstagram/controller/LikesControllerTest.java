package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.makeComponent.MakeComponent;
import com.cho.songstagram.service.FollowService;
import com.cho.songstagram.service.LikesService;
import com.cho.songstagram.service.PostsService;
import com.cho.songstagram.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class LikesControllerTest {

    @Autowired UsersService usersService;
    @Autowired PostsService postsService;
    @Autowired LikesService likesService;
    @Autowired FollowService followService;
    @Autowired MockMvc mockMvc;
    MakeComponent makeComponent = new MakeComponent();

    @Test
    public void 좋아요_테스트() throws Exception {
        Users users = makeComponent.makeUsers();
        usersService.save(users);

        Posts posts = makeComponent.makePosts(users);
        postsService.save(posts);

        mockMvc.perform(MockMvcRequestBuilders.get("/likes/save/"+posts.getId()+"&"+users.getId())
                .sessionAttr("loginUser",users))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }


}