package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Likes;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.FollowListDto;
import com.cho.songstagram.exception.NoResultException;
import com.cho.songstagram.service.FollowService;
import com.cho.songstagram.service.LikesService;
import com.cho.songstagram.service.PostsService;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.Set;

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
                               @PathVariable("userId") Long userId) throws NoResultException {
        Users users = usersService.findById(userId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다."));
        Posts posts = postsService.findById(postId).orElseThrow(() -> new NoResultException("잘못된 Post 정보 입니다."));
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
                                 @PathVariable("userId") Long userId) throws NoResultException {
        Users users = usersService.findById(userId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다."));
        Posts posts = postsService.findById(postId).orElseThrow(() -> new NoResultException("잘못된 Post 정보 입니다."));
        Likes likes = likesService.findByPostsAndUsers(posts, users).orElseGet(Likes::new); // 게시글에 user가 누른 좋아요 객체 가져오기
        likesService.delete(likes); // 해당 좋아요 정보 db에서 삭제
        return "redirect:/post/read/{postId}";
    }

    // 게시글에 좋아요 누른 사람 목록 보여주기
    @GetMapping("/likes/userList/{postId}&{userId}")
    public String likeUserListGet(@PathVariable("postId") Long postId,
                                  @PathVariable("userId") Long userId,
                                  Model model) throws NoResultException {
        Posts posts = postsService.findById(postId).orElseGet(Posts::new);
        Users loginUser = usersService.findById(userId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다."));
        Set<Long> likeUserIdList = likesService.findLikeUserIdList(posts); // 게시글에 좋아요 누른 user id 목록 가져오기

        Set<FollowListDto> userDto = new HashSet<>();
        for (Long likeUserId : likeUserIdList) {
            Users users = usersService.findById(likeUserId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다.")); // 좋아요 누른 user id로 user 가져오기
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
