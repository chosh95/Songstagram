package com.cho.songstagram.controller;

import com.cho.songstagram.dto.CommentDto;
import com.cho.songstagram.service.CommentsService;
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

    // 댓글 등록 컨트롤러
    @PostMapping("/comment/write/{postId}&{userId}")
    public String commentWritePost(@PathVariable("postId") Long postId,
                               @PathVariable("userId") Long userId,
                               @ModelAttribute("commentDto")CommentDto commentDto){
        commentsService.save(postId,userId,commentDto);
        return "redirect:/post/read/{postId}";
    }

    // 댓글 삭제 컨트롤러
    @GetMapping("/comment/delete/{commentId}&{postId}")
    public String commentDeleteGet(@PathVariable("commentId") Long commentId,
                                @PathVariable("postId") Long postId){
        commentsService.delete(commentId);
        return "redirect:/post/read/{postId}";
    }

    // 댓글 삭제 실패 컨트롤러 : 댓글 작성자와 사용자가 다른 경우
    @GetMapping("/comment/noAuthority")
    public String notDeleteGet(){
        return "comment/noAuthority";
    }
}
