package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.DeleteUserDto;
import com.cho.songstagram.dto.LoginUserDto;
import com.cho.songstagram.dto.SignInUserDto;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.Binding;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/user/login")
    public String loginUserGet(@ModelAttribute("loginUserDto") LoginUserDto loginUserDto){
        return "/user/login";
    }

    @PostMapping("/user/login")
    public String loginUserPost(@Valid @ModelAttribute("loginUserDto") LoginUserDto loginUserDto,
                            BindingResult result, Model model, HttpSession session){

        Users users = usersService.findByEmail(loginUserDto.getEmail())
                .orElse(new Users());

        if(result.hasErrors() || users.getId()==null || !users.matchPassword(loginUserDto.getPassword())){
            if(users.getId()==null || !users.matchPassword(loginUserDto.getPassword()))
                model.addAttribute("idPwMsg","아이디, 비밀번호를 다시 확인해주세요.");
            return "/user/login";
        }
        if(session.getAttribute("loginUser") != null)
            session.removeAttribute("loginUser");
        session.setAttribute("loginUser",users);

        return "redirect:/";
    }

    @GetMapping("/user/doLogin")
    public String doLogin(){
        return "/user/doLogin";
    }

    @GetMapping("/user/logout")
    public String logoutUserGet(HttpSession session){
        session.invalidate();
        return "/user/logout";
    }

    @GetMapping("/user/signIn")
    public String signInUserGet(@ModelAttribute("signInUserDto") SignInUserDto signInUserDto){
        return "/user/signIn";
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
            return "/user/signIn";
        }

        String picture = addFile(files);

        Users newUser = Users.builder()
                .password(signInUserDto.getPassword())
                .email(signInUserDto.getEmail())
                .name(signInUserDto.getName())
                .picture(picture)
                .build();

        usersService.addUser(newUser);

        return "redirect:/user/login";
    }

    @GetMapping("/user/profile/{userId}")
    public String profileUserGet(@PathVariable("userId") Long userId, HttpSession session, Model model){
        Users users = usersService.findById(userId)
                .orElse(new Users());
        List<Posts> postsList = users.getPostsList();
        postsList.sort(new ListComparator());
        model.addAttribute("postsList",postsList);
        model.addAttribute("userId",users.getId());
        return "/user/profile";
    }

    @GetMapping("/user/update/{userId}")
    public String updateUserGet(@PathVariable("userId") Long userId, Model model){
        Users users = usersService.findById(userId)
                .orElse(new Users());
        model.addAttribute("user",users);
        return "/user/update";
    }

    @GetMapping("/user/delete/{userId}")
    public String deleteUserGet(@PathVariable("userId") Long userId,
                                @ModelAttribute("deleteUserDto")DeleteUserDto deleteUserDto, Model model){
        model.addAttribute("userId",userId);
        return "/user/delete";
    }

    @PostMapping("/user/delete/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId,
                             @Valid @ModelAttribute("deleteUserDto")DeleteUserDto deleteUserDto,
                             BindingResult result, Model model, HttpSession session){
        Users users = usersService.findById(userId).orElse(new Users());
        if(!users.matchPassword(deleteUserDto.getPassword())){
            model.addAttribute("pwMsg","비밀번호가 틀렸습니다.");
            model.addAttribute("userId",userId);
            return "/user/delete";
        }
        if(result.hasErrors()){
            model.addAttribute("userId",userId);
            return "/user/delete";
        }
        usersService.delete(users);
        session.invalidate();
        return "redirect:/";
    }

    public String addFile(MultipartFile files) throws IOException {
        if(files.isEmpty()) return "profile.png";
        UUID uuid = UUID.randomUUID();
        String newName = uuid.toString() + "_" + files.getOriginalFilename();
        String baseDir = "C:\\git\\Songstagram\\uploads\\profile\\";
        files.transferTo(new File(baseDir + newName));
        return newName;
    }

    private static class ListComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            LocalDateTime a = ((Posts)o1).getCreatedDate();
            LocalDateTime b = ((Posts)o2).getCreatedDate();
            return b.compareTo(a);
        }
    }
}
