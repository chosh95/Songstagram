package com.cho.songstagram.config;

import com.cho.songstagram.domain.Follow;
import com.cho.songstagram.interceptor.*;
import com.cho.songstagram.service.CommentsService;
import com.cho.songstagram.service.FollowService;
import com.cho.songstagram.service.PostsService;
import com.cho.songstagram.service.UsersService;
import org.hibernate.engine.jdbc.connections.internal.UserSuppliedConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class HomeConfig implements WebMvcConfigurer {

    @Autowired
    CommentsService commentsService;
    @Autowired
    PostsService postsService;
    @Autowired
    UsersService usersService;
    @Autowired
    FollowService followService;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //로그인 인터셉터
        registry.addInterceptor(new HomeInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/icon/**")
                .excludePathPatterns("/uploads/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/doLogin")
                .excludePathPatterns("/user/signIn");
        //댓글 삭제 인터셉터
        registry.addInterceptor(new CommentDeleteInterceptor(commentsService))
                .addPathPatterns("/comment/delete/**");
        //게시글 수정 & 삭제 인터셉터
        registry.addInterceptor(new PostInterceptor(postsService))
                .addPathPatterns("/post/delete/**")
                .addPathPatterns("/post/update/**");
        //회원 수정 & 삭제 인터셉터
        registry.addInterceptor(new UserInterceptor(usersService))
                .addPathPatterns("/user/update/**")
                .addPathPatterns("/user/delete/**");
        registry.addInterceptor(new FollowInterceptor(followService,usersService))
                .addPathPatterns("/follow/**");
    }

}
