package com.cho.songstagram.interceptor;

import com.cho.songstagram.domain.IpBanList;
import com.cho.songstagram.repository.IpBanRepository;
import com.cho.songstagram.service.IpBanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

@RequiredArgsConstructor
public class PostWriteInterceptor extends HandlerInterceptorAdapter {

    private final IpBanService ipBanService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(ipBanService.isIpBan(request)){
            response.sendRedirect("/post/writePostLimit");
            return false;
        }
        return true;
    }
}
