package com.cho.songstagram.interceptor;

import com.cho.songstagram.domain.Users;
import com.cho.songstagram.service.FollowService;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RequiredArgsConstructor
public class FollowInterceptor extends HandlerInterceptorAdapter {

    private final FollowService followService;
    private final UsersService usersService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Users loginUser = (Users)session.getAttribute("loginUser"); // 세션에서 로그인 유저 가져오기
        
        Map<String,String> attribute = (Map<String,String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE); // Model 에서 값 가져오기 위함
        String userId = attribute.get("userId"); 
        Users users = usersService.findById(Long.parseLong(userId)).orElseGet(Users::new); // 유저 정보 가져오기

        if(followService.isFollowing(loginUser,users)) { // 로그인한 유저가 선택한 유저를 이미 팔로우 중인지 확인
            response.sendRedirect("/follow/already");
            return false;
        }
        return true;
    }
}
