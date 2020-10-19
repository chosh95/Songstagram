package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.FollowListDto;
import com.cho.songstagram.exception.NoResultException;
import com.cho.songstagram.service.FollowService;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final UsersService usersService;

    // 팔로우 컨트롤러
    @GetMapping("/follow/{userId}&{loginUserId}")
    public String followGet(@PathVariable("userId") Long userId,
                         @PathVariable("loginUserId") Long loginUserId,
                         HttpServletRequest request) throws NoResultException {
        Users from = usersService.findById(loginUserId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다."));
        Users to = usersService.findById(userId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다."));
        followService.save(from,to); //로그인 유저가 선택한 유저를 팔로우

        // 이전 페이지로 복귀
        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

    // 언팔로우 컨트롤러
    @GetMapping("/unfollow/{userId}&{loginUserId}")
    public String unfollowGet(@PathVariable("userId") Long userId,
                           @PathVariable("loginUserId") Long loginUserId,
                           HttpServletRequest request) throws NoResultException {

        Users from = usersService.findById(loginUserId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다."));
        Users to = usersService.findById(userId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다."));

        followService.delete(from,to); //로그인 유저가 선택한 유저를 언팔로우

        // 이전 페이지로 복귀
        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

    // 팔로워 목록 확인 컨트롤러
    @GetMapping("/followerList/{userId}")
    public String followerListGet(@PathVariable("userId") Long userId,
                               HttpSession session, Model model) {

        Users loginUser = (Users) session.getAttribute("loginUser");

        Set<Users> follower = followService.getFollower(userId); // 선택한 유저의 팔로워 목록
        Set<FollowListDto> followDto = new HashSet<>(); // dto list에 담아서 view에 전환
        for (Users users : follower) {
            followDto.add(followService.convertDto(loginUser, users)); // 팔로워 유저들 dto로 전환
        }
        
        model.addAttribute("followDto",followDto); // 팔로워 유저들 dto list
        model.addAttribute("userId",userId); //다시 프로필로 되돌아오기 위한 userId
        
        return "follow/followerList";
    }

    // 팔로잉 목록 확인 컨트롤러
    @GetMapping("/followingList/{userId}")
    public String followingListGet(@PathVariable("userId") Long userId,
                                HttpSession session, Model model) {

        Users loginUser = (Users) session.getAttribute("loginUser");

        Set<Users> follower = followService.getFollowing(userId); // 선택한 유저의 팔로잉 목록
        Set<FollowListDto> followDto = new HashSet<>();
        for (Users users : follower) {
            followDto.add(followService.convertDto(loginUser, users)); // 팔로잉 유저들 dto로 전환
        }
        
        model.addAttribute("followDto",followDto); // 팔로잉 유저 dto list
        model.addAttribute("userId",userId); //다시 프로필로 되돌아오기 위한 userId

        return "follow/followingList";
    }

    // 이미 팔로잉 중인 사용자 팔로우 시 보여주는 페이지 맵핑
    @GetMapping("/follow/already")
    public String alreadyGet(){
        return "follow/already";
    }
}
