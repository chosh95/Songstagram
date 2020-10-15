package com.cho.songstagram.controller;

import com.cho.songstagram.exception.NoResultException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
@ControllerAdvice("com.cho.songstagram.controller")
public class ExceptionController {

    @ExceptionHandler({RuntimeException.class, NoResultException.class})
    public String noResultHandle(Exception e, Model model){
        String errorMessage = e.getMessage();
        model.addAttribute("errorMessage",errorMessage);
        return "error/noResult";
    }

}
