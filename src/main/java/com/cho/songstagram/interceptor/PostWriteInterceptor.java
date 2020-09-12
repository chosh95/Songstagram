package com.cho.songstagram.interceptor;

import com.cho.songstagram.domain.IpBanList;
import com.cho.songstagram.repository.IpBanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

@RequiredArgsConstructor
public class PostWriteInterceptor extends HandlerInterceptorAdapter {

    private final IpBanRepository ipBanRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        List<IpBanList> list = ipBanRepository.findAll();
        if(!list.isEmpty()){
            for(IpBanList ip : list){
                if(request.getRemoteAddr().matches(ip.getIp())){
                    response.setContentType("test/html; charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.println("<script>alert('ip 차단으로 글 작성이 제한됩니다.');history.go(-1);</script>");
                    out.flush();
                    return false;
                }
            }
        }
        return true;
    }
}
