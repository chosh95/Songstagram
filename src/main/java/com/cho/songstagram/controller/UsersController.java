package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.*;
import com.cho.songstagram.exception.NoResultException;
import com.cho.songstagram.service.FollowService;
import com.cho.songstagram.service.PostsService;
import com.cho.songstagram.service.S3Service;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UsersController {

    private final PostsService postsService;
    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final FollowService followService;

    // 로그인 화면
    @GetMapping("/user/login")
    public String loginUserGet(@ModelAttribute("loginUserDto") LoginUserDto loginUserDto){
        return "user/login";
    }

    // 로그인 처리
    @PostMapping("/user/login")
    public String loginUserPost(@Valid @ModelAttribute("loginUserDto") LoginUserDto loginUserDto,
                            BindingResult result, Model model, HttpSession session) throws NoResultException {

        Users users = usersService.findByEmail(loginUserDto.getEmail()).orElse(new Users()); // 이메일로 유저 확인

        //오류 검사. id, pw가 틀리면 메세지 전송
        if(users.getId()==null || !passwordEncoder.matches(loginUserDto.getPassword(),users.getPassword())) {
            model.addAttribute("idPwMsg", "아이디, 비밀번호를 다시 확인해주세요.");
            return "user/login";
        }
        //그 외 다른 오류 처리
        if(result.hasErrors()){
            return "user/login";
        }

        //로그인 성공 시
        //loginUser 세션 관리 : 있으면 지우고 다시 생성, 없으면 바로 생성
        if(session.getAttribute("loginUser") != null)
            session.removeAttribute("loginUser");
        session.setAttribute("loginUser",users);

        return "redirect:/";
    }

    //로그인 요청 화면
    @GetMapping("/user/doLogin")
    public String doLoginGet(){
        return "user/doLogin";
    }

    //로그아웃 처리
    @GetMapping("/user/logout")
    public String logoutUserGet(HttpSession session){
        session.invalidate(); //로그아웃 시 loginUser 세션 지우기
        return "user/logout";
    }

    //회원 가입 입력 화면
    @GetMapping("/user/signIn")
    public String signInUserGet(@ModelAttribute("signInUserDto") SignInUserDto signInUserDto){
        return "user/signIn";
    }

    //회원 가입 처리
    @PostMapping("/user/signIn")
    public String signInUserPost(@Valid @ModelAttribute("signInUserDto") SignInUserDto signInUserDto,
                         BindingResult result, Model model, @RequestParam("files") MultipartFile files) throws IOException, NoResultException {

        Users users = usersService.findByEmail(signInUserDto.getEmail()).orElse(new Users()); // 중복 확인

        if(result.hasErrors() || users.getId()!=null || !signInUserDto.matchPassword()){
            if(users.getId()!=null) // 아이디 중복 시
                model.addAttribute("emailExist","이메일이 이미 존재합니다");
            if(!signInUserDto.matchPassword()) // 비밀번호 오류
                model.addAttribute("passwordMsg","확인 비밀번호가 다릅니다.");
            return "user/signIn";
        }

        String picture = s3Service.userUpload(files); //S3 버킷에 프로필 이미지 업로드

        Users newUser = Users.builder()
                .password(passwordEncoder.encode(signInUserDto.getPassword())) //BCrypt 방식으로 암호화
                .email(signInUserDto.getEmail())
                .name(signInUserDto.getName())
                .picture(picture)
                .build();

        usersService.save(newUser); // db에 유저 저장
        
        return "redirect:/user/login"; // 로그인 화면으로 유도
    }

    //프로필 화면
    @GetMapping("/user/profile/{userId}")
    public String profileUserGet(@RequestParam(value = "page",defaultValue = "1") int page,
                                 @PathVariable("userId") Long userId,
                                 HttpSession session, Model model) throws NoResultException {

        Users users = usersService.findUserFetchById(userId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다.")); // fetch를 통해 작성글 목록, 팔로워, 팔로잉 정보 한 번에 가져오기

        int postByUserCnt = users.getPostsList().size(); // 유저가 작성한 게시글 수
        model.addAttribute("postsCnt", postByUserCnt);

        List<PostDto> postDtoList = postsService.getUserPostList(users, page, 5); // 유저가 작성한 글 페이지에 맞게 5개 가져오기
        model.addAttribute("postDtoList",postDtoList);

        ProfileUserDto userDto = new ProfileUserDto(users.getId(),users.getName(),users.getPicture()); // 프로필 dto
        model.addAttribute("userDto",userDto);

        PageDto pageDto = new PageDto(page,5,users.getPostsList().size(),5); // 페이지 정보
        model.addAttribute("pageDto",pageDto);

        Users loginUser = (Users) session.getAttribute("loginUser"); // 로그인 유저 가져오기
        boolean follow = followService.isFollowing(loginUser,users); //로그인한 사람이 프로필 유저 팔로잉 중인지 여부
        model.addAttribute("follow",follow);

        int followerCnt = users.getFollower().size(); // 팔로워 수
        int followingCnt = users.getFollowing().size(); // 팔로잉 수
        model.addAttribute("follower",followerCnt);
        model.addAttribute("following",followingCnt);

        return "user/profile";
    }

    //유저 정보 수정 페이지
    @GetMapping("/user/update/{userId}")
    public String updateUserGet(@PathVariable("userId") Long userId,
                                @ModelAttribute("updateUserDto") UpdateUserDto updateUserDto) throws NoResultException {
        Users users = usersService.findById(userId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다."));
        
        updateUserDto.setName(users.getName()); // 기본 정보 제공
        updateUserDto.setPicture(users.getPicture());
        
        return "user/update";
    }

    //유저 정보 수정 Post
    @PostMapping("/user/update/{userId}")
    public String updateUserPost(@PathVariable("userId") Long userId, @RequestParam("files") MultipartFile files,
                                 @Valid @ModelAttribute("updateUserDto") UpdateUserDto updateUserDto,
                                 BindingResult result, HttpSession session) throws IOException, NoResultException {
        Users users = usersService.findById(userId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다.")); //유저 정보 가져오기

        if(result.hasErrors()) // 입력값에 오류 있을시
            return "user/update";
        if(!users.getPicture().equals("https://elasticbeanstalk-us-east-2-089357406904.s3.us-east-2.amazonaws.com/upload/profile/profile.png"))
            s3Service.deleteUser(users.getPicture()); // 기본 프로필 이미지가 아니면, 프로필 이미지 S3에서 삭제
        
        String picture = s3Service.userUpload(files); // 새로운 이미지 S3에 업로드
        
        users.updatePicture(picture);
        users.updateName(updateUserDto.getName());
        usersService.update(users); //유저 정보 db에 저장
        
        session.setAttribute("loginUser",users); //새로운 유저 정보로 세션 덮어쓰기

        return "redirect:/user/profile/{userId}";
    }

    //회원 탈퇴 요청 화면
    @GetMapping("/user/delete/{userId}")
    public String deleteUserGet(@PathVariable("userId") Long userId,
                                @ModelAttribute("deleteUserDto")DeleteUserDto deleteUserDto, Model model){
        model.addAttribute("userId",userId); //아이디 정보 제공
        
        return "user/delete";
    }

    //회원 탈퇴 처리
    @PostMapping("/user/delete/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId,
                             @Valid @ModelAttribute("deleteUserDto")DeleteUserDto deleteUserDto,
                             BindingResult result, Model model, HttpSession session) throws NoResultException {
        
        Users users = usersService.findById(userId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다.")); //유저 정보 가져오기
        
        if(!passwordEncoder.matches(deleteUserDto.getPassword(),users.getPassword())){ //확인 비밀번호 일치 여부 확인
            model.addAttribute("pwMsg","비밀번호가 틀렸습니다.");
            model.addAttribute("userId",userId);
            return "user/delete";
        }
        if(result.hasErrors()){ //그 이외 에러. @NotBlank, @Size 등
            model.addAttribute("userId",userId);
            return "user/delete";
        }
        
        //S3에서 프로필 이미지 삭제. 기본 프로필 아닌 경우만.
        if(!users.getPicture().equals("https://elasticbeanstalk-us-east-2-089357406904.s3.us-east-2.amazonaws.com/upload/profile/profile.png"))
            s3Service.deleteUser(users.getPicture());
        //user가 작성한 post 사진들 삭제
        for (Posts posts : users.getPostsList()) {
            s3Service.deletePost(posts.getPicture());
        }
        
        usersService.delete(users); //db에서 유저 삭제

        session.invalidate(); //로그인 세션 삭제

        return "redirect:/";
    }

    // 수정 & 삭제 권한 없을시 인터셉터에서 여기로 요청
    @GetMapping("/user/noAuthority")
    public String notUpdate(){
        return "user/noAuthority";
    }
}
