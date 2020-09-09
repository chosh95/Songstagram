package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Likes;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.service.LikesService;
import com.cho.songstagram.service.PostsService;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequiredArgsConstructor
public class LikesController {

    private final UsersService usersService;
    private final PostsService postsService;
    private final LikesService likesService;

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
        Likes likes = likesService.findByPostsAndUsers(posts, users).orElse(new Likes());
        likesService.delete(likes); // 좋아요 취소
        return "redirect:/post/read/{postId}";
    }

    // 이미 좋아요 한 경우 보여주는 페이지 맵핑
    @GetMapping("/likes/already")
    public String likesAlready(){
        return "/likes/already";
    }
}
