package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.CommentDto;
import com.cho.songstagram.dto.PostDto;
import com.cho.songstagram.service.CommentsService;
import com.cho.songstagram.service.PostsService;
import com.cho.songstagram.service.S3Service;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PostsController {

    private final UsersService usersService;
    private final PostsService postsService;
    private final CommentsService commentsService;
    private final S3Service s3Service;

    @GetMapping("/post/write")
    public String write(@ModelAttribute("postDto") PostDto postDto){
        return "post/write";
    }

    @PostMapping("/post/write")
    public String writePost(@Valid @ModelAttribute("postDto") PostDto postDto, BindingResult result,
                            @RequestParam("files") MultipartFile files,
                            HttpSession session, Model model) throws IOException {

        if(files.isEmpty()){
            model.addAttribute("emptyFileMsg","사진을 입력해주세요.");
            return "post/write";
        }
        if(result.hasErrors()){
            return "post/write";
        }

        String picture = s3Service.postUpload(files);

        Users loginUser = (Users)session.getAttribute("loginUser");
        Users user = usersService.findByEmail(loginUser.getEmail())
                .orElse(new Users());

        Posts post = Posts.builder()
                .singer(postDto.getSinger())
                .songName(postDto.getSongName())
                .content(postDto.getContent())
                .picture(picture)
                .users(user)
                .build();

        postsService.save(post);
        return "redirect:/";
    }

    @GetMapping("/post/read/{post_id}")
    public String readPost(@PathVariable("post_id") Long postId, @ModelAttribute("commentDto") CommentDto commentDto, Model model){
        Posts posts = postsService.findById(postId)
                .orElse(new Posts());
        PostDto postDto = postsService.convertToDto(posts);
        model.addAttribute("post",postDto);
        
        List<Comments> commentsList = commentsService.findCommentsByPosts(posts);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comments comments : commentsList) {
            commentDtoList.add(commentsService.convertToDto(comments));
        }
        model.addAttribute("commentsList",commentDtoList);

        return "post/read";
    }

    @GetMapping("/post/update/{post_id}")
    public String update(@PathVariable("post_id") Long postId, @ModelAttribute("postDto") PostDto postDto, Model model){
        Posts posts = postsService.findById(postId)
                .orElse(new Posts());
        postDto.setContent(posts.getContent());
        postDto.setPicture(posts.getPicture());
        postDto.setSinger(posts.getSinger());
        postDto.setSongName(posts.getSongName());
        model.addAttribute("postId",postId);
        return "post/update";
    }

    @PostMapping("/post/update/{post_id}")
    public String updatePost(@PathVariable("post_id") Long postId,
                             @Valid @ModelAttribute("postDto") PostDto postDto, BindingResult result, Model model){

        if(result.hasErrors()) {
            model.addAttribute("postId",postId);
            return "post/update";
        }

        Posts posts = postsService.findById(postId)
                .orElse(new Posts());
        posts.update(postDto.getSinger(), postDto.getSongName(), postDto.getContent());
        postsService.save(posts);
        return "redirect:/post/read/{post_id}";
    }

    @GetMapping("/post/delete/{post_id}")
    public String delete(@PathVariable("post_id") Long postId){
        Posts posts = postsService.findById(postId)
                .orElse(new Posts());
        s3Service.deletePost(posts.getPicture());
        postsService.delete(posts);
        return "post/delete";
    }

}
