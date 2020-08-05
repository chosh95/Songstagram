package com.cho.songstagram.config;

import com.cho.songstagram.interceptor.DeleteInterceptor;
import com.cho.songstagram.interceptor.HomeInterceptor;
import com.cho.songstagram.service.CommentsService;
import org.hibernate.service.spi.InjectService;
import org.hibernate.sql.Delete;
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DeleteInterceptor(commentsService))
                .addPathPatterns("/comment/delete/**");
        registry.addInterceptor(new HomeInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/icon/**")
                .excludePathPatterns("/uploads/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/doLogin")
                .excludePathPatterns("/user/signIn");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
