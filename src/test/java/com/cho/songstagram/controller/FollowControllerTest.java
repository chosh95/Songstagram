package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.FollowListDto;
import com.cho.songstagram.makeComponent.MakeComponent;
import com.cho.songstagram.service.FollowService;
import com.cho.songstagram.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class FollowControllerTest {

    @Autowired FollowService followService;
    @Autowired UsersService usersService;
    @Autowired MockMvc mockMvc;
    MakeComponent makeComponent = new MakeComponent();

    @Test
    public void 팔로우_테스트() throws Exception {

        Users users = makeComponent.makeUsers();
        usersService.save(users);
        Users users1 = makeComponent.makeUsers("cho@cho.com");
        usersService.save(users1);

        mockMvc.perform(get("/follow/" + users1.getId().toString() + "&" + users.getId().toString())
                .sessionAttr("loginUser",users))
                .andExpect(status().is3xxRedirection());

        assertTrue(followService.isFollowing(users,users1));
    }

    @Test
    public void 언팔로우_테스트() throws Exception {
        Users users = makeComponent.makeUsers();
        usersService.save(users);
        Users users1 = makeComponent.makeUsers("cho@cho.com");
        usersService.save(users1);

        followService.save(users,users1);
        assertTrue(followService.isFollowing(users,users1));

        mockMvc.perform(get("/unfollow/" + users1.getId().toString() + "&" + users.getId().toString())
                .sessionAttr("loginUser",users))
                .andExpect(status().is3xxRedirection());

        assertFalse(followService.isFollowing(users,users1));
    }

    @Test
    public void 팔로잉_목록() throws Exception{
        Users users = makeComponent.makeUsers();
        usersService.save(users);
        Users users1 = makeComponent.makeUsers("cho1@cho.com");
        usersService.save(users1);
        Users users2 = makeComponent.makeUsers("cho2@cho.com");
        usersService.save(users2);

        followService.save(users,users1);
        followService.save(users,users2);

        Set<FollowListDto> followDto = new HashSet<>(); // dto list에 담아서 view에 전환
        for (Users users3 : followService.getFollowing(users.getId())) {
            followDto.add(followService.convertDto(users, users3)); // 팔로워 유저들 dto로 전환
        }

        mockMvc.perform(get("/followingList/" + users.getId().toString())
                .sessionAttr("loginUser",users))
                .andExpect(status().isOk())
                .andExpect(view().name("follow/followingList"))
                .andExpect(model().attribute("userId",users.getId()))
                .andExpect(model().attribute("followDto",followDto));
    }

    @Test
    public void 팔로워_목록() throws Exception{
        Users users = makeComponent.makeUsers();
        usersService.save(users);
        Users users1 = makeComponent.makeUsers("cho1@cho.com");
        usersService.save(users1);
        Users users2 = makeComponent.makeUsers("cho2@cho.com");
        usersService.save(users2);

        followService.save(users1,users);
        followService.save(users2, users);

        Set<FollowListDto> followDto = new HashSet<>(); // dto list에 담아서 view에 전환
        for (Users users3 : followService.getFollower(users.getId())) {
            followDto.add(followService.convertDto(users, users3)); // 팔로워 유저들 dto로 전환
        }

        mockMvc.perform(get("/followerList/" + users.getId().toString())
                .sessionAttr("loginUser",users))
                .andExpect(status().isOk())
                .andExpect(view().name("follow/followerList"))
                .andExpect(model().attribute("userId",users.getId()))
                .andExpect(model().attribute("followDto",followDto));
    }

    @Test
    public void 이미_팔로우_중일때() throws Exception {
        Users users = makeComponent.makeUsers("abcd@abcd.com");
        usersService.save(users);

        mockMvc.perform(get("/follow/already")
                .sessionAttr("loginUser",users))
                .andExpect(status().isOk())
                .andExpect(view().name("follow/already"));
    }
}