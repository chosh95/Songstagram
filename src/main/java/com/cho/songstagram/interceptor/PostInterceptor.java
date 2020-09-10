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
        Users loginUser = (Users)session.getAttribute("loginUser"); // 세션에서 로그인 유저 가져오기

        Map<String,String> attribute = (Map<String,String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE); // Model 에서 값 가져오기
        String postId = attribute.get("postId"); 
        Posts posts = postsService.findById(Long.parseLong(postId)).orElse(new Posts()); // 작성글 가져오기

        if(!posts.getUsers().getId().equals(loginUser.getId())) { // 게시글 작성자가 로그인한 유저가 아니면 차단
            response.sendRedirect("/post/noAuthority");
            return false;
        }

        return true;
    }
}
