package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.CommentDto;
import com.cho.songstagram.dto.PageDto;
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
    public String writeGet(@ModelAttribute("postDto") PostDto postDto) {
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
        Users users = usersService.findById(loginUser.getId()).orElse(new Users()); // 영속성 컨텍스트에서 유저 초기화
        Posts posts = postsService.makePost(postDto,users,picture); // Posts 객체 생성
        postsService.save(posts); // db에 저장

        return "redirect:/";
    }

    @GetMapping("/post/read/{postId}")
    public String readGet(@PathVariable("postId") Long postId,
                          @ModelAttribute("commentDto") CommentDto commentDto,
                          Model model) {
        Posts posts = postsService.findById(postId).orElse(new Posts()); // postId로 게시글 찾기
        PostDto postDto = postsService.convertToDto(posts); // 게시글 보여줄 dto로 전환
        model.addAttribute("post", postDto); //model에 dto 추가

        List<Comments> commentsList = commentsService.findCommentsByPosts(posts); // 게시글의 댓글 가져오기
        List<CommentDto> commentDtoList = new ArrayList<>(); //dto로 전환해서 반환할 list
        for (Comments comments : commentsList) 
            commentDtoList.add(commentsService.convertToDto(comments)); // dto 전환
        model.addAttribute("commentsList", commentDtoList); // model에 댓글 dto 추가

        String youtubeLink = "https://www.youtube.com/results?search_query="; // 유튜브 링크 생성
        youtubeLink += postDto.getSinger() + "+" + postDto.getSongName(); // 가수명과 곡 제목으로 링크 완성
        model.addAttribute("youtubeLink",youtubeLink); 

        return "post/read";
    }

    @GetMapping("/post/update/{postId}")
    public String updateGet(@PathVariable("postId") Long postId, @ModelAttribute("postDto") PostDto postDto, Model model) {
        Posts posts = postsService.findById(postId).orElse(new Posts()); // 게시글 id로 찾아오기
        postDto.setContent(posts.getContent()); // dto에 post 정보 넣어서 기존 정보 제공
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
            model.addAttribute("postId", postId); //에러 있을시 다시 update 화면으로
            return "post/update";
        }

        Posts posts = postsService.findById(postId).orElse(new Posts()); // 게시글 찾아와서
        posts.update(postDto.getSinger(), postDto.getSongName(), postDto.getContent()); // update 한 후
        postsService.save(posts); // db에 저장
        return "redirect:/post/read/{postId}";
    }

    // 좋아요 누른 목록 보여주는 controller
    @GetMapping("/post/likeList/{userId}")
    public String likeListGet(@RequestParam(value = "page", defaultValue = "1") int page,
                           @PathVariable("userId") Long userId, Model model) {
        List<PostDto> postDtoList = postsService.getUserLikeListPage(userId, page, 5); // 유저가 좋아요 한 게시글 postDto로 전환 후 가져오기
        model.addAttribute("postDtoList", postDtoList);

        Users users = usersService.findById(userId).orElse(new Users()); // 유저가 누른 좋아요 size 구하기 위해 user 가져옴
        PageDto pageDto = new PageDto(page, 5, users.getLikesList().size(), 5); // 페이지네이션
        model.addAttribute("pageDto", pageDto);

        return "post/likeList";
    }

    // 팔로우 한 사람의 게시글 목록
    @GetMapping("/post/followList/{userId}")
    public String followListGet(@RequestParam(value = "page", defaultValue = "1") int page,
                             @PathVariable("userId") Long userId, Model model){
        List<PostDto> postDtoList = postsService.getFollowListPage(userId, page, 5); //유저가 팔로우 한 사람의 게시글 페이지에 맞게 5개 가져오기
        model.addAttribute("postDtoList",postDtoList);

        PageDto pageDto = new PageDto(page,5, Math.toIntExact(postsService.getFollowPostCount(userId)), 5); //페이지네이션
        model.addAttribute("pageDto",pageDto);

        return "post/followList";
    }

    // 게시글 삭제 기능
    @GetMapping("/post/delete/{postId}")
    public String deleteGet(@PathVariable("postId") Long postId){
        Posts posts = postsService.findById(postId).orElse(new Posts()); // 게시글 찾기
        s3Service.deletePost(posts.getPicture()); // S3 버킷에 올린 사진 삭제
        postsService.delete(posts); // 게시글 db에서 삭제
        return "post/delete";
    }

    // 권한 없이 접근 했을시
    @GetMapping("/post/noAuthority")
    public String noAuthorityGet(){
        return "post/noAuthority";
    } // 작성자가 아닐시 수정 & 삭제 불가능

}
