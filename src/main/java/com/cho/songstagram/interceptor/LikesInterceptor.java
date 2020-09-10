package com.cho.songstagram.interceptor;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.service.LikesService;
import com.cho.songstagram.service.PostsService;
import com.cho.songstagram.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RequiredArgsConstructor
public class LikesInterceptor extends HandlerInterceptorAdapter {

    private final LikesService likesService;
    private final UsersService usersService;
    private final PostsService postsService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String,String> attribute = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String userId = attribute.get("userId");
        Users users = usersService.findById(Long.parseLong(userId)).orElse(new Users());
        
        String postId = attribute.get("postId");
        Posts posts = postsService.findById(Long.parseLong(postId)).orElse(new Posts());

        if(likesService.findByPostsAndUsers(posts, users).isPresent()){ // 유저가 이미 좋아요 누른 게시글이면 차단
            response.sendRedirect("/likes/already");
            return false;
        }

        return true;
    }
}
