package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.FollowListDto;
import com.cho.songstagram.service.FollowService;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final UsersService usersService;

    @GetMapping("/follow/{userId}&{loginUserId}")
    public String follow(@PathVariable("userId") Long userId,
                         @PathVariable("loginUserId") Long loginUserId,
                         HttpServletRequest request){
        Users from = usersService.findById(loginUserId).orElse(new Users());
        Users to = usersService.findById(userId).orElse(new Users());
        followService.save(from,to);

//      이전 페이지로 복귀
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/unfollow/{userId}&{loginUserId}")
    public String unfollow(@PathVariable("userId") Long userId,
                           @PathVariable("loginUserId") Long loginUserId,
                           HttpServletRequest request){
        Users from = usersService.findById(loginUserId).orElse(new Users());
        Users to = usersService.findById(userId).orElse(new Users());
        followService.delete(from,to);

//      이전 페이지로 복귀
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/followerList/{userId}")
    public String followerList(@PathVariable("userId") Long userId,
                               HttpSession session, Model model) {
        Users loginUser = (Users) session.getAttribute("loginUser");
        List<Users> follower = followService.getFollower(userId);
        List<FollowListDto> followDto = new ArrayList<>();
        for (Users users : follower) {
            followDto.add(followService.convertDto(loginUser, users));
        }
        model.addAttribute("followDto",followDto);
        model.addAttribute("userId",userId); //다시 프로필로 되돌아오기 위한 userId
        return "follow/followerList";
    }

    @GetMapping("/followingList/{userId}")
    public String followingList(@PathVariable("userId") Long userId,
                                HttpSession session, Model model) {
        Users loginUser = (Users) session.getAttribute("loginUser");
        List<Users> follower = followService.getFollowing(userId);
        List<FollowListDto> followDto = new ArrayList<>();
        for (Users users : follower) {
            followDto.add(followService.convertDto(loginUser, users));
        }
        model.addAttribute("followDto",followDto);
        model.addAttribute("userId",userId); //다시 프로필로 되돌아오기 위한 userId
        return "follow/followingList";
    }
}
