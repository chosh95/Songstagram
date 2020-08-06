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

    @PostMapping("/comment/write/{postId}&{userId}")
    public String commentWrite(@PathVariable("postId") Long postId,
                               @PathVariable("userId") Long userId,
                               @ModelAttribute("commentDto")CommentDto commentDto){
        commentsService.save(postId,userId,commentDto);
        return "redirect:/post/read/{postId}";
    }

    @GetMapping("/comment/delete/{commentId}&{postId}")
    public String commentDelete(@PathVariable("commentId") Long commentId,
                                @PathVariable("postId") Long postId){
        commentsService.delete(commentId);
        return "redirect:/post/read/{postId}";
    }

    @GetMapping("/comment/notDelete")
    public String notDelete(){
        return "comment/notDelete";
    }
}
