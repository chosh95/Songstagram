package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.*;
import com.cho.songstagram.service.FollowService;
import com.cho.songstagram.service.PostsService;
import com.cho.songstagram.service.S3Service;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/user/login")
    public String loginUserGet(@ModelAttribute("loginUserDto") LoginUserDto loginUserDto){
        return "user/login";
    }

    @PostMapping("/user/login")
    public String loginUserPost(@Valid @ModelAttribute("loginUserDto") LoginUserDto loginUserDto,
                            BindingResult result, Model model, HttpSession session){

        Users users = usersService.findByEmail(loginUserDto.getEmail())
                .orElse(new Users());

        //오류 검사. id, pw가 틀리면 메세지 전송
        if(users.getId()==null || !passwordEncoder.matches(loginUserDto.getPassword(),users.getPassword())) {
            model.addAttribute("idPwMsg", "아이디, 비밀번호를 다시 확인해주세요.");
            return "user/login";
        }
        if(result.hasErrors()){
            return "user/login";
        }

        // loginUser 세션 관리 있으면 지우고 다시 생성, 없으면 바로 생성
        if(session.getAttribute("loginUser") != null)
            session.removeAttribute("loginUser");
        session.setAttribute("loginUser",users);

        return "redirect:/";
    }

    @GetMapping("/user/doLogin")
    public String doLogin(){
        return "user/doLogin";
    }

    @GetMapping("/user/logout")
    public String logoutUserGet(HttpSession session){
        session.invalidate(); //로그아웃 시 loginUser 세션 지우기
        return "user/logout";
    }

    @GetMapping("/user/signIn")
    public String signInUserGet(@ModelAttribute("signInUserDto") SignInUserDto signInUserDto){
        return "user/signIn";
    }

    @PostMapping("/user/signIn")
    public String signInUserPost(@Valid @ModelAttribute("signInUserDto") SignInUserDto signInUserDto,
                         BindingResult result, Model model, @RequestParam("files") MultipartFile files) throws IOException {

        Users users = usersService.findByEmail(signInUserDto.getEmail())
                .orElse(new Users());

        if(result.hasErrors() || users.getId()!=null || !signInUserDto.matchPassword()){
            if(users.getId()!=null)
                model.addAttribute("emailExist","이메일이 이미 존재합니다");
            if(!signInUserDto.matchPassword())
                model.addAttribute("passwordMsg","확인 비밀번호가 다릅니다.");
            return "user/signIn";
        }

        String picture = s3Service.userUpload(files);

        Users newUser = Users.builder()
                .password(passwordEncoder.encode(signInUserDto.getPassword())) //BCrypt 방식으로 암호화
                .email(signInUserDto.getEmail())
                .name(signInUserDto.getName())
                .picture(picture)
                .build();

        usersService.save(newUser);
        return "redirect:/user/login";
    }

    @GetMapping("/user/profile/{userId}")
    public String profileUserGet(@RequestParam(value = "page",defaultValue = "1") int page,
                                 @PathVariable("userId") Long userId,
                                 HttpSession session, Model model){

        Users users = usersService.findById(userId)
                .orElse(new Users());
        List<PostDto> postDtoList = postsService.getUserPostList(users, page, 5);
        model.addAttribute("postDtoList",postDtoList);

        ProfileUserDto userDto = new ProfileUserDto(users.getId(),users.getName(),users.getPicture());
        model.addAttribute("userDto",userDto);

        PageDto pageDto = new PageDto(page,5,users.getPostsList().size(),5);
        model.addAttribute("pageDto",pageDto);

        Users loginUser = (Users) session.getAttribute("loginUser");
        boolean follow = followService.isFollowing(loginUser,users);
        model.addAttribute("follow",follow);

        Long followerCnt = followService.countFollower(users);
        Long followingCnt = followService.countFollowing(users);
        model.addAttribute("follower",followerCnt);
        model.addAttribute("following",followingCnt);

        return "user/profile";
    }

    @GetMapping("/user/update/{userId}")
    public String updateUserGet(@PathVariable("userId") Long userId,
                                @ModelAttribute("updateUserDto") UpdateUserDto updateUserDto){
        Users users = usersService.findById(userId)
                .orElse(new Users());
        updateUserDto.setName(users.getName());
        updateUserDto.setPicture(users.getPicture());
        return "user/update";
    }

    @PostMapping("/user/update/{userId}")
    public String updateUserPost(@PathVariable("userId") Long userId, @RequestParam("files") MultipartFile files,
                                 @Valid @ModelAttribute("updateUserDto") UpdateUserDto updateUserDto,
                                 BindingResult result, HttpSession session) throws IOException {
        Users users = usersService.findById(userId)
                .orElse(new Users());
        if(result.hasErrors()){
            return "user/update";
        }
        if(!users.getPicture().equals("https://elasticbeanstalk-us-east-2-089357406904.s3.us-east-2.amazonaws.com/upload/profile/profile.png"))
            s3Service.deleteUser(users.getPicture());
        String picture = s3Service.userUpload(files);
        users.updatePicture(picture);
        users.updateName(updateUserDto.getName());
        usersService.save(users);
        session.setAttribute("loginUser",users); //세션 덮어쓰기
        return "redirect:/user/profile/{userId}";
    }

    @GetMapping("/user/delete/{userId}")
    public String deleteUserGet(@PathVariable("userId") Long userId,
                                @ModelAttribute("deleteUserDto")DeleteUserDto deleteUserDto, Model model){
        model.addAttribute("userId",userId);
        return "user/delete";
    }

    @PostMapping("/user/delete/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId,
                             @Valid @ModelAttribute("deleteUserDto")DeleteUserDto deleteUserDto,
                             BindingResult result, Model model, HttpSession session){
        
        Users users = usersService.findById(userId).orElse(new Users());
        if(!passwordEncoder.matches(deleteUserDto.getPassword(),users.getPassword())){
            model.addAttribute("pwMsg","비밀번호가 틀렸습니다.");
            model.addAttribute("userId",userId);
            return "user/delete";
        }
        if(result.hasErrors()){
            model.addAttribute("userId",userId);
            return "user/delete";
        }
        
        //user 프로필 사진 삭제
        if(!users.getPicture().equals("https://elasticbeanstalk-us-east-2-089357406904.s3.us-east-2.amazonaws.com/upload/profile/profile.png"))
            s3Service.deleteUser(users.getPicture());
        //user가 작성한 post 사진들 삭제
        for (Posts posts : users.getPostsList()) {
            s3Service.deletePost(posts.getPicture());
        }
        
        usersService.delete(users);
        session.invalidate();
        return "redirect:/";
    }
}
