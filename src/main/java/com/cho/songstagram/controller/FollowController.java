package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Users;
import com.cho.songstagram.service.FollowService;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final UsersService usersService;

    @GetMapping("/follow/{userId}&{loginUserId}")
    public String follow(@PathVariable("userId") Long userId,
                         @PathVariable("loginUserId") Long loginUserId){
        Users to = usersService.findById(userId).orElse(new Users());
        Users from = usersService.findById(loginUserId).orElse(new Users());
        followService.save(from,to);
        return "redirect:/user/profile/{userId}&{loginUserId}";
    }

    @GetMapping("/unfollow/{userId}&{loginUserId}")
    public String unfollow(@PathVariable("userId") Long userId,
                           @PathVariable("loginUserId") Long loginUserId){
        Users to = usersService.findById(userId).orElse(new Users());
        Users from = usersService.findById(loginUserId).orElse(new Users());
        followService.delete(from,to);
        return "redirect:/user/profile/{userId}&{loginUserId}";
    }
}
