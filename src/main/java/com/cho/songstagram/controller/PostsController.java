package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Likes;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.CommentDto;
import com.cho.songstagram.dto.PageDto;
import com.cho.songstagram.dto.PostDto;
import com.cho.songstagram.repository.PostsRepository;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostsController {

    private final UsersService usersService;
    private final PostsService postsService;
    private final CommentsService commentsService;
    private final S3Service s3Service;

    //게시글 작성 페이지 맵핑
    @GetMapping("/post/write")
    public String write(@ModelAttribute("postDto") PostDto postDto) {
        return "post/write";
    }

    //게시글 작성 처리 컨트롤러
    @PostMapping("/post/write")
    public String writePost(@Valid @ModelAttribute("postDto") PostDto postDto, BindingResult result,
                            @RequestParam("files") MultipartFile files,
                            HttpSession session, Model model) throws IOException {

        if (files.isEmpty()) { // 사진 파일 추가 안했을 시 추가하도록 유도
            model.addAttribute("emptyFileMsg", "사진을 추가해주세요.");
            return "post/write";
        }
        if (result.hasErrors()) { // 가수, 곡, 내용에 오류가 있을 시 다시 작성
            return "post/write";
        }

        String picture = s3Service.postUpload(files); // S3 버킷에 파일 업로드
        Users loginUser = (Users) session.getAttribute("loginUser"); // loginUser 세션에서 가져오기
        loginUser = usersService.findById(loginUser.getId()).orElse(new Users()); // 영속성 컨텍스트 업데이트
        Posts posts = postsService.makePost(postDto,loginUser,picture); // Posts 객체 생성
        postsService.save(posts); // db에 저장

        return "redirect:/";
    }

    @GetMapping("/post/read/{postId}")
    public String readPost(@PathVariable("postId") Long postId, @ModelAttribute("commentDto") CommentDto commentDto, Model model) {
        Posts posts = postsService.findById(postId)
                .orElse(new Posts());
        PostDto postDto = postsService.convertToDto(posts);
        model.addAttribute("post", postDto);

        List<Comments> commentsList = commentsService.findCommentsByPosts(posts);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comments comments : commentsList) {
            commentDtoList.add(commentsService.convertToDto(comments));
        }
        model.addAttribute("commentsList", commentDtoList);

        String youtubeLink = "https://www.youtube.com/results?search_query=";
        youtubeLink += postDto.getSinger() + "+" + postDto.getSongName();
        model.addAttribute("youtubeLink",youtubeLink);
        return "post/read";
    }

    @GetMapping("/post/update/{postId}")
    public String update(@PathVariable("postId") Long postId, @ModelAttribute("postDto") PostDto postDto, Model model) {
        Posts posts = postsService.findById(postId)
                .orElse(new Posts());
        postDto.setContent(posts.getContent());
        postDto.setPicture(posts.getPicture());
        postDto.setSinger(posts.getSinger());
        postDto.setSongName(posts.getSongName());
        model.addAttribute("postId", postId);
        return "post/update";
    }

    @PostMapping("/post/update/{postId}")
    public String updatePost(@PathVariable("postId") Long postId,
                             @Valid @ModelAttribute("postDto") PostDto postDto, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("postId", postId);
            return "post/update";
        }

        Posts posts = postsService.findById(postId)
                .orElse(new Posts());
        posts.update(postDto.getSinger(), postDto.getSongName(), postDto.getContent());
        postsService.save(posts);
        return "redirect:/post/read/{postId}";
    }

    //   좋아요 누른 목록 보여주는 controller
    @GetMapping("/post/likeList/{userId}")
    public String likeList(@RequestParam(value = "page", defaultValue = "1") int page,
                           @PathVariable("userId") Long userId, Model model) {
        List<PostDto> postDtoList = postsService.getUserLikeListPage(userId, page, 5);
        Users users = usersService.findById(userId).orElse(new Users());
        PageDto pageDto = new PageDto(page, 5, users.getLikesList().size(), 5);

        model.addAttribute("postDtoList", postDtoList);
        model.addAttribute("pageDto", pageDto);

        return "post/likeList";
    }

    @GetMapping("/post/followList/{userId}")
    public String followList(@RequestParam(value = "page", defaultValue = "1") int page,
                             @PathVariable("userId") Long userId, Model model){
        List<PostDto> postDtoList = postsService.getFollowListPage(userId, page, 5);
        PageDto pageDto = new PageDto(page,5, Math.toIntExact(postsService.getFollowPostCount(userId)), 5);

        model.addAttribute("postDtoList",postDtoList);
        model.addAttribute("pageDto",pageDto);
        return "post/followList";
    }

    @GetMapping("/post/delete/{postId}")
    public String delete(@PathVariable("postId") Long postId){
        Posts posts = postsService.findById(postId)
                .orElse(new Posts());
        s3Service.deletePost(posts.getPicture());
        postsService.delete(posts);
        return "post/delete";
    }

    @GetMapping("/post/notUpdate")
    public String notUpdate(){
        return "post/notUpdate";
    }

}
