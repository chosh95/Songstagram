package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.CommentDto;
import com.cho.songstagram.dto.PageDto;
import com.cho.songstagram.dto.PostDto;
import com.cho.songstagram.exception.NoResultException;
import com.cho.songstagram.service.CommentsService;
import com.cho.songstagram.service.IpBanService;
import com.cho.songstagram.service.PostsService;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PostsController {

    private final UsersService usersService;
    private final PostsService postsService;
    private final CommentsService commentsService;
    private final IpBanService ipBanService;

    //게시글 작성 페이지 맵핑
    @GetMapping("/post/write")
    public String writeGet(@ModelAttribute("postDto") PostDto postDto) {
        return "post/write";
    }

    //게시글 작성 처리 컨트롤러
    @PostMapping("/post/write")
    public String writePost(@Valid @ModelAttribute("postDto") PostDto postDto, BindingResult result,
                            @RequestParam("files") MultipartFile files,
                            HttpServletRequest request,
                            HttpSession session, Model model) throws IOException {

        if (files.isEmpty()) { // 사진 파일 추가 안했을 시 추가하도록 유도
            model.addAttribute("emptyFileMsg", "사진을 추가해주세요.");
            return "post/write";
        }
        if (result.hasErrors()) { // 가수, 곡, 내용에 오류가 있을 시 다시 작성
            return "post/write";
        }

        Users loginUser = (Users) session.getAttribute("loginUser"); // loginUser 세션에서 가져오기

        Long postsCntByUserToday = postsService.getPostsCntByUserToday(loginUser.getId()); // 작성자가 오늘 작성한 글의 수를 구한다.
        if (postsCntByUserToday >= 4) { // 하루에 4번 이상 글을 작성했을 시
            ipBanService.banIp(request); // 해당 request로 들어온 ip를 차단한다.
            return "redirect:/post/writePostLimit"; // 글은 작성되지 않고 redirect된다.
        }

        String picture = addFile(files);
        Posts posts = postsService.makePost(postDto, loginUser.getId(), picture); // Posts 객체 생성
        postsService.save(posts); // db에 저장

        return "redirect:/";
    }

    // { post, user, likelist -> (3번) }, { comment, user -> ( 1 + N번) } 걸리던 쿼리를 fetch join을 활용해 2번으로 줄였다.
    // 게시글 확인하는 컨트롤러
    @GetMapping("/post/read/{postId}")
    public String readGet(@PathVariable("postId") Long postId,
                          @ModelAttribute("commentDto") CommentDto commentDto,
                          Model model) {
        Posts posts = postsService.findByPostId(postId); // fetch join으로 작성자와 좋아요 목록까지 가져오기
        PostDto postDto = postsService.convertToDto(posts); // 게시글 보여줄 dto로 전환
        model.addAttribute("post", postDto); //model에 dto 추가

        Set<Comments> commentsList = commentsService.findCommentsAndUsersByPosts(posts); // 댓글 목록과 사용자 정보 한번에 가져오기

        Set<CommentDto> commentDtoList = new HashSet<>(); //dto로 전환해서 반환할 list
        for (Comments comments : commentsList)
            commentDtoList.add(commentsService.convertToDto(comments)); // dto 전환
        model.addAttribute("commentsList", commentDtoList); // model에 댓글 dto 추가

        String youtubeLink = "https://www.youtube.com/results?search_query=" + postDto.getSinger() + "+" + postDto.getSongName(); // 유튜브 링크 생성
        model.addAttribute("youtubeLink", youtubeLink);

        return "post/read";
    }

    // 게시글 업데이트 화면으로 연결하는 컨트롤러
    @GetMapping("/post/update/{postId}")
    public String updateGet(@PathVariable("postId") Long postId, @ModelAttribute("postDto") PostDto postDto, Model model) throws NoResultException {
        Posts posts = postsService.findById(postId).orElseThrow(() -> new NoResultException("잘못된 Post 정보 입니다.")); // 게시글 id로 찾아오기

        postDto.setContent(posts.getContent()); // dto에 post 정보 넣어서 기존 정보 제공
        postDto.setSinger(posts.getSinger());
        postDto.setSongName(posts.getSongName());

        model.addAttribute("postId", postId);

        return "post/update";
    }

    // 게시글 업데이트 처리 컨트롤러
    @PostMapping("/post/update/{postId}")
    public String updatePost(@PathVariable("postId") Long postId,
                             @Valid @ModelAttribute("postDto") PostDto postDto, BindingResult result, Model model) throws NoResultException {

        if (result.hasErrors()) {
            model.addAttribute("postId", postId); //에러 있을시 다시 update 화면으로
            return "post/update";
        }

        Posts posts = postsService.findById(postId).orElseThrow(() -> new NoResultException("잘못된 Post 정보 입니다.")); // 게시글 찾아와서
        posts.update(postDto.getSinger(), postDto.getSongName(), postDto.getContent()); // update 한 후
        postsService.update(posts); // db에 저장

        return "redirect:/post/read/{postId}";
    }

    // 좋아요 누른 목록 보여주는 controller
    @GetMapping("/post/likeList/{userId}")
    public String likeListGet(@RequestParam(value = "page", defaultValue = "1") int page,
                              @PathVariable("userId") Long userId, Model model) throws NoResultException {
        List<PostDto> postDtoList = postsService.getUserLikeListPage(userId, page, 5); // 유저가 좋아요 한 게시글 postDto로 전환 후 가져오기
        model.addAttribute("postDtoList", postDtoList);

        Users users = usersService.findById(userId).orElseThrow(() -> new NoResultException("잘못된 Post 정보 입니다.")); // 유저가 누른 좋아요 size 구하기 위해 user 가져옴
        PageDto pageDto = new PageDto(page, 5, users.getLikesList().size(), 5); // 페이지네이션
        model.addAttribute("pageDto", pageDto);

        return "post/likeList";
    }

    // 팔로우 한 사람의 게시글 목록
    @GetMapping("/post/followList/{userId}")
    public String followListGet(@RequestParam(value = "page", defaultValue = "1") int page,
                                @PathVariable("userId") Long userId, Model model) {
        List<PostDto> postDtoList = postsService.getFollowListPage(userId, page, 5); //유저가 팔로우 한 사람의 게시글 페이지에 맞게 5개 가져오기
        model.addAttribute("postDtoList", postDtoList);

        PageDto pageDto = new PageDto(page, 5, Math.toIntExact(postsService.getFollowPostCount(userId)), 5); //페이지네이션
        model.addAttribute("pageDto", pageDto);

        return "post/followList";
    }

    // 게시글 삭제 기능
    @GetMapping("/post/delete/{postId}")
    public String deleteGet(@PathVariable("postId") Long postId) throws NoResultException {
        Posts posts = postsService.findById(postId).orElseThrow(() -> new NoResultException("잘못된 Post 정보 입니다.")); // 게시글 찾기

        removeFile(posts.getPicture());
        postsService.delete(posts); // 게시글 db에서 삭제

        return "post/delete";
    }

    // 권한 없이 접근 했을시
    @GetMapping("/post/noAuthority")
    public String noAuthorityGet() {
        return "post/noAuthority";
    } // 작성자가 아닐시 수정 & 삭제 불가능

    //ip 차단되었음을 알리는 페이지
    @GetMapping("/post/writePostLimit")
    public String writePostLimitGet() {
        return "post/writePostLimit";
    }

    public String addFile(MultipartFile files) throws IOException {
        if (files.isEmpty()) return null;
        UUID uuid = UUID.randomUUID();
        String newName = uuid + "_" + files.getOriginalFilename();
        String baseDir = "C:\\git\\Songstagram\\uploads\\post\\";
        files.transferTo(new File(baseDir + newName));
        return newName;
    }

    public void removeFile(String path) {
        String originalPath = "C:\\git\\Songstagram\\uploads\\post\\" + path;
        File file = new File(originalPath);
        if (file.delete()) {
            System.out.println("delete Success");
        }
    }
}
