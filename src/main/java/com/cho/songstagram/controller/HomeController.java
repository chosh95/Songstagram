package com.cho.songstagram.controller;

import com.cho.songstagram.dto.PageDto;
import com.cho.songstagram.dto.PostDto;
import com.cho.songstagram.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostsService postsService;

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/surf")
    public String surf(@RequestParam(value = "page", defaultValue = "1") int page,
                       Model model){
        List<PostDto> postsList = postsService.getPostList(page, 5);
        PageDto pageDto = new PageDto(page,5,Math.toIntExact(postsService.getPostsCount()),5);

        model.addAttribute("postsList",postsList);
        model.addAttribute("pageDto",pageDto);

        return "surf";
    }
}
