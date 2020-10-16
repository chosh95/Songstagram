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
    public String home(@RequestParam(value = "page", defaultValue = "1") int page,
                       Model model){
        // 개시글을 한 페이지에 5개씩 보여줄 때 해당 페이지에 맞는 게시글들 가져오기
        List<PostDto> postsList = postsService.getPostList(page, 5); 
        
        // 게시글을 한 페이지에 5개씩, 하단에 5개의 페이지씩 페이지네이션하는 dto.
        PageDto pageDto = new PageDto(page,5,Math.toIntExact(postsService.getPostsCount()),5);

        model.addAttribute("postsList",postsList);
        model.addAttribute("pageDto",pageDto);

        return "index";
    }

}
