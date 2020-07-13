package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.CommentDto;
import com.cho.songstagram.service.CommentsService;
import com.cho.songstagram.service.PostsService;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class CommentController {

    private final PostsService postsService;
    private final UsersService usersService;
    private final CommentsService commentsService;

    @PostMapping("/comment/write/{post_id}&{user_id}")
    public String commentWrite(@PathVariable("post_id") Long postId,
                               @PathVariable("user_id") Long userId,
                               @ModelAttribute("commentDto")CommentDto commentDto){

        Posts posts = postsService.findById(postId)
                .orElse(new Posts());
        Users users = usersService.findById(userId)
                .orElse(new Users());
        Comments comments = Comments.builder()
                .content(commentDto.getComment())
                .users(users)
                .posts(posts)
                .build();

        commentsService.save(comments);

        return "redirect:/post/read/{post_id}";
    }
}
