package com.cho.songstagram.interceptor;

import com.cho.songstagram.domain.Users;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class HomeInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Users loginUser = (Users)session.getAttribute("loginUser"); // 세션에서 유저 정보 가져오기
        
        if(loginUser==null){ // 없으면 로그인 하도록 유도
            response.sendRedirect("/user/doLogin");
            return false;
        }
        return true;
    }
}
