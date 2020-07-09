package com.cho.songstagram.controller;

import com.cho.songstagram.dto.PostDto;
import com.cho.songstagram.service.PostsService;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class PostsController {

    private final UsersService usersService;
    private final PostsService postsService;

    @GetMapping("/post/write")
    public String write(@ModelAttribute("postDto") PostDto postDto){
        return "/post/write";
    }

    @PostMapping("/post/write")
    public String writePost(@Valid @ModelAttribute("postDto") PostDto postDto,
                            @RequestParam("files") MultipartFile files, BindingResult result,
                            HttpSession session, Model model){

        return "redirect:/";
    }
}
