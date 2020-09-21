package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Likes;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.FollowListDto;
import com.cho.songstagram.service.FollowService;
import com.cho.songstagram.service.LikesService;
import com.cho.songstagram.service.PostsService;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LikesController {

    private final UsersService usersService;
    private final PostsService postsService;
    private final LikesService likesService;
    private final FollowService followService;

    // 해당 게시글에 좋아요하는 컨트롤러
    @GetMapping("/likes/save/{postId}&{userId}")
    public String saveLikesGet(@PathVariable("postId") Long postId,
                               @PathVariable("userId") Long userId) throws IOException {
        Users users = usersService.findById(userId).orElse(new Users());
        Posts posts = postsService.findById(postId).orElse(new Posts());
        Likes likes = Likes.builder() // 유저와 게시글 id에 맞게 like 객체 생성
                .posts(posts)
                .users(users)
                .build();
        likesService.save(likes);
        return "redirect:/post/read/{postId}";
    }

    // 게시글에 좋아요 취소 컨트롤러
    @GetMapping("/likes/delete/{postId}&{userId}")
    public String deleteLikesGet(@PathVariable("postId") Long postId,
                                 @PathVariable("userId") Long userId){
        Users users = usersService.findById(userId).orElse(new Users());
        Posts posts = postsService.findById(postId).orElse(new Posts());
        Likes likes = likesService.findByPostsAndUsers(posts, users).orElse(new Likes()); // 게시글에 user가 누른 좋아요 객체 가져오기
        likesService.delete(likes); // 해당 좋아요 정보 db에서 삭제
        return "redirect:/post/read/{postId}";
    }

    // 게시글에 좋아요 누른 사람 목록 보여주기
    @GetMapping("/likes/userList/{postId}&{userId}")
    public String likeUserListGet(@PathVariable("postId") Long postId,
                                  @PathVariable("userId") Long userId,
                                  Model model){
        Posts posts = postsService.findById(postId).orElse(new Posts());
        Users loginUser = usersService.findById(userId).orElse(new Users());
        List<Long> likeUserIdList = likesService.findLikeUserIdList(posts); // 게시글에 좋아요 누른 user id 목록 가져오기

        List<FollowListDto> userDto = new ArrayList<>();
        for (Long likeUserId : likeUserIdList) {
            Users users = usersService.findById(likeUserId).orElse(new Users()); // 좋아요 누른 user id로 user 가져오기
            userDto.add(followService.convertDto(loginUser, users)); // 좋아요 누른 user 정보들 dto로 전환, 팔로우 여부까지 표시
        }

        model.addAttribute("userDto",userDto);
        model.addAttribute("postId",postId);
        return "likes/likeUserList";
    }

    // 이미 좋아요 한 경우 보여주는 페이지 맵핑
    @GetMapping("/likes/already")
    public String likesAlready(){
        return "/likes/already";
    }
}
