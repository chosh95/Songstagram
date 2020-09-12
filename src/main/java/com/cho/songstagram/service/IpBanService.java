package com.cho.songstagram.service;

import com.cho.songstagram.domain.IpBanList;
import com.cho.songstagram.repository.IpBanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IpBanService {

    private final IpBanRepository ipBanRepository;

    @Transactional
    public IpBanList save(IpBanList ipBanList){
        return ipBanRepository.save(ipBanList);
    }

    @Transactional
    public void banIp(HttpServletRequest request) { // 해당 요청으로 들어온 ip를 차단하는 메서드
        String ipAddress = this.getIpAddress(request); // ip 주소를 찾아온다.
        if(ipBanRepository.findByIp(ipAddress)==null){ // 해당 ip를 차단한다.
            IpBanList ipBanList = new IpBanList(ipAddress);
            this.save(ipBanList);
        }
    }

    public boolean isIpBan(HttpServletRequest request){
        String ipAddress = this.getIpAddress(request);
        return ipBanRepository.findByIp(ipAddress)!=null ;
    }

    public String getIpAddress(HttpServletRequest request){
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
