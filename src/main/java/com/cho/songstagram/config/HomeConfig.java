package com.cho.songstagram.config;

import com.cho.songstagram.interceptor.HomeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class HomeConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HomeInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/icon/**")
                .excludePathPatterns("/uploads/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/doLogin")
                .excludePathPatterns("/user/signIn");
    }
}
