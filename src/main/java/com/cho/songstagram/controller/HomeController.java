package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostsService postsService;

    @GetMapping("/")
    public String home(Model model){
        Pageable firstPage = PageRequest.of(0,9, Sort.by("createdDate").descending());
        Page<Posts> page = postsService.findAll(firstPage);
        List<Posts> posts = page.getContent();
        model.addAttribute("postsList",posts);
        return "index";
    }
}
