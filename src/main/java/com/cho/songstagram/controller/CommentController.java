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

    private final CommentsService commentsService;

    @PostMapping("/comment/write/{post_id}&{user_id}")
    public String commentWrite(@PathVariable("post_id") Long postId,
                               @PathVariable("user_id") Long userId,
                               @ModelAttribute("commentDto")CommentDto commentDto){
        commentsService.save(postId,userId,commentDto);
        return "redirect:/post/read/{post_id}";
    }

    @GetMapping("/comment/delete/{comment_id}&{post_id}")
    public String commentDelete(@PathVariable("comment_id") Long commentId,
                                @PathVariable("post_id") Long postId){
        commentsService.delete(commentId);
        return "redirect:/post/read/{post_id}";
    }
}
