package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.CommentDto;
import com.cho.songstagram.dto.PostDto;
import com.cho.songstagram.service.CommentsService;
import com.cho.songstagram.service.PostsService;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PostsController {

    private final UsersService usersService;
    private final PostsService postsService;
    private final CommentsService commentsService;

    @GetMapping("/post/write")
    public String write(@ModelAttribute("postDto") PostDto postDto){
        return "/post/write";
    }

    @PostMapping("/post/write")
    public String writePost(@Valid @ModelAttribute("postDto") PostDto postDto, BindingResult result,
                            @RequestParam("files") MultipartFile files,
                            HttpSession session, Model model) throws IOException {

        if(files.isEmpty()){
            model.addAttribute("emptyFileMsg","사진을 입력해주세요.");
            return "/post/write";
        }
        if(result.hasErrors()){
            return "/post/write";
        }

        String picture = addFile(files);

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
        model.addAttribute("post",posts);
        List<Comments> commentsList = commentsService.findCommentsByPosts(posts);
        model.addAttribute("commentsList",commentsList);
        return "/post/read";
    }

    @GetMapping("/post/updateGet/{post_id}")
    public String update(@PathVariable("post_id") Long postId, @ModelAttribute("postDto") PostDto postDto, Model model){
        Posts posts = postsService.findById(postId)
                .orElse(new Posts());
        postDto.setContent(posts.getContent());
        postDto.setPicture(posts.getPicture());
        postDto.setSinger(posts.getSinger());
        postDto.setSongName(posts.getSongName());
        postDto.setUserId(posts.getUsers().getId());
        model.addAttribute("postId",postId);
        return "/post/update";
    }

    @PostMapping("/post/update/{post_id}")
    public String updatePost(@PathVariable("post_id") Long postId,
                             @Valid @ModelAttribute("postDto") PostDto postDto, BindingResult result, Model model){

        if(result.hasErrors()) {
            model.addAttribute("postId",postId);
            return "/post/update";
        }

        Posts posts = postsService.findById(postId)
                .orElse(new Posts());
        posts.update(postDto.getSinger(), postDto.getSongName(), postDto.getContent());
        postsService.save(posts);
        return "redirect:/post/read/{post_id}";
    }

    public String addFile(MultipartFile files) throws IOException {
        if(files.isEmpty()) return null;
        UUID uuid = UUID.randomUUID();
        String newName = uuid.toString() + "_" + files.getOriginalFilename();
        String baseDir = "C:\\git\\Songstagram\\uploads\\post\\";
        files.transferTo(new File(baseDir + newName));
        return newName;
    }

}
