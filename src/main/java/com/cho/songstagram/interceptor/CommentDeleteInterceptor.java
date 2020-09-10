package com.cho.songstagram.interceptor;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RequiredArgsConstructor
public class CommentDeleteInterceptor extends HandlerInterceptorAdapter {

    private final CommentsService commentsService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Users loginUser = (Users)session.getAttribute("loginUser"); // 세션에서 로그인 유저 가져오기
        
        Map<String,String> attribute = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE); //Model에 있는 값 가져오기
        String commentId = attribute.get("commentId"); //댓글 id로 댓글 객체 가져오기
        Comments comments = commentsService.findById(Long.parseLong(commentId)).orElse(new Comments()); 
        
        if(!comments.getUsers().getId().equals(loginUser.getId())) { // 로그인 유저 아이디와 댓글단 아이디가 다르면 권한 없음
            response.sendRedirect("/comment/noAuthority");
            return false;
        }
        return true;
    }
}
