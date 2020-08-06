package com.cho.songstagram.interceptor;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RequiredArgsConstructor
public class PostInterceptor extends HandlerInterceptorAdapter {

    private final PostsService postsService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Users loginUser = (Users)session.getAttribute("loginUser");
        Map attribute = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String postId = (String) attribute.get("postId");
        Posts posts = postsService.findById(Long.parseLong(postId)).orElse(new Posts());
        if(!posts.getUsers().getId().equals(loginUser.getId())) {
            response.sendRedirect("/post/notUpdate");
            return false;
        }
        return true;
    }
}
