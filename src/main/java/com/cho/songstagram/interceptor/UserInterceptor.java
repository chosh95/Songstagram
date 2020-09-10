package com.cho.songstagram.interceptor;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RequiredArgsConstructor
public class UserInterceptor extends HandlerInterceptorAdapter {

    private final UsersService usersService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Users loginUser = (Users)session.getAttribute("loginUser"); // 세션에서 로그인 유저 가져오기

        Map<String,String> attribute = (Map<String,String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE); // Model에서 값 가져오기
        String userId = attribute.get("userId");
        Users users = usersService.findById(Long.parseLong(userId)).orElse(new Users()); // 유저 정보 가져오기

        if(!users.getId().equals(loginUser.getId())) { // 로그인 유저와 수정/삭제하려는 유저가 다르면 차단
            response.sendRedirect("/user/noAuthority");
            return false;
        }
        return true;
    }
}
