package com.cho.songstagram.interceptor;

import com.cho.songstagram.service.IpBanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class PostWriteInterceptor extends HandlerInterceptorAdapter {

    private final IpBanService ipBanService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(ipBanService.isIpBan(request)){
            response.sendRedirect("/post/writePostLimit"); // 차단되었음을 알려주는 페이지로 redirect
            return false; //차단한다.
        }
        return true;
    }
}
