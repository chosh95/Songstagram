package com.cho.songstagram.interceptor;

import com.cho.songstagram.domain.Posts;
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
        Users loginUser = (Users)session.getAttribute("loginUser");
        Map<String,String> attribute = (Map<String,String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String userId = attribute.get("userId");
        Users users = usersService.findById(Long.parseLong(userId)).orElse(new Users());

        if(followService.isFollowing(loginUser,users)) {
            response.sendRedirect("/follow/already");
            return false;
        }
        return true;
    }
}
