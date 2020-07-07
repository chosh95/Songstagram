package com.cho.songstagram.controller;

import com.cho.songstagram.domain.Users;
import com.cho.songstagram.domain.dto.UserDto;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(UserDto userDto, HttpSession session){
        Users users = usersService.findByEmail(userDto.getEmail())
                .orElse(new Users());

        if(users.getId()==null || !users.matchPassword(userDto.getPassword())){
            return "redirect:/login";
        }

        session.setAttribute("loginUser",users);

        return "redirect:/";
    }

    @GetMapping("/signIn")
    public String signIn(){
        return "signIn";
    }

    @PostMapping("/singIn")
    public String signInPro(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result){
        if(result.hasErrors()){
            return "signIn";
        }
        Users users = Users.builder()
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .picture(userDto.getPicture())
                .name(userDto.getName())
                .build();
        usersService.addUser(users);

        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.setAttribute("loginUser",null);
        return "logout";
    }
}
