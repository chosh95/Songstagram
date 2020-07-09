package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.LoginUserDto;
import com.cho.songstagram.dto.SignInUserDto;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/user/login")
    public String login(@ModelAttribute("loginUserDto") LoginUserDto loginUserDto){
        return "/user/login";
    }

    @PostMapping("/user/login")
    public String loginPost(@Valid @ModelAttribute("loginUserDto") LoginUserDto loginUserDto,
                            BindingResult result, Model model, HttpSession session){

        Users users = usersService.findByEmail(loginUserDto.getEmail())
                .orElse(new Users());

        if(result.hasErrors() || users.getId()==null || !users.matchPassword(loginUserDto.getPassword())){
            if(users.getId()==null || !users.matchPassword(loginUserDto.getPassword()))
                model.addAttribute("idPwMsg","아이디, 비밀번호를 다시 확인해주세요.");
            return "/user/login";
        }

        session.setAttribute("loginUser",users);

        return "redirect:/";
    }

    @GetMapping("/user/signIn")
    public String signIn(@ModelAttribute("signInUserDto") SignInUserDto signInUserDto){
        return "/user/signIn";
    }

    @PostMapping("/user/signIn")
    public String upload(@Valid @ModelAttribute("signInUserDto") SignInUserDto signInUserDto,
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

        String baseDir = "C:\\git\\Songstagram\\uploads\\";
        String originalName = files.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String newName = uuid.toString() + "_" + originalName;

        files.transferTo(new File(baseDir + newName));

        System.out.println(signInUserDto.getEmail());
        Users newUser = Users.builder()
                .password(signInUserDto.getPassword())
                .email(signInUserDto.getEmail())
                .picture(newName)
                .name(signInUserDto.getName())
                .build();
        usersService.addUser(newUser);

        return "redirect:/user/login";
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession session){
        session.removeAttribute("loginUser");
        return "/user/logout";
    }

}
