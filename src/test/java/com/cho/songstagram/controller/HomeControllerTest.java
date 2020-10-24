package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.PageDto;
import com.cho.songstagram.dto.PostDto;
import com.cho.songstagram.makeComponent.MakeComponent;
import com.cho.songstagram.service.PostsService;
import com.cho.songstagram.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired PostsService postsService;
    @Autowired UsersService usersService;
    @Autowired MockMvc mockMvc;
    MakeComponent makeComponent = new MakeComponent();

    @Test
    public void 홈_테스트() throws Exception {
        Users users = makeComponent.makeUsers();
        usersService.save(users);

        Posts posts = makeComponent.makePosts(users);
        postsService.save(posts);

        List<PostDto> postsList = postsService.getPostList(1, 5);
        PageDto pageDto = new PageDto(1,5,Math.toIntExact(postsService.getPostsCount()),5);

        mockMvc.perform(get("/")
                .sessionAttr("loginUser",users)
                .param("page", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("pageDto",pageDto))
                .andExpect(model().attribute("postsList",postsList));
    }

}