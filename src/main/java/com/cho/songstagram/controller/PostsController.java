package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.PostDto;
import com.cho.songstagram.service.PostsService;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

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
    public String writePost(@ModelAttribute("postDto") PostDto postDto,
                            @RequestParam("files") MultipartFile files,
                            HttpSession session, Model model) throws IOException {

        if(files.isEmpty()){
            model.addAttribute("emptyFileMsg","사진을 입력해주세요.");
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
    public String readPost(@PathVariable("post_id") Long postId, Model model){
        Posts posts = postsService.findById(postId)
                .orElse(new Posts());
        model.addAttribute("post",posts);
        return "post/read";
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
