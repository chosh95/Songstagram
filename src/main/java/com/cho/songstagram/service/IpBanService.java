package com.cho.songstagram.service;

import com.cho.songstagram.domain.IpBanList;
import com.cho.songstagram.repository.IpBanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

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
            IpBanList ipBanList = new IpBanList(ipAddress); // ip 개체 생성한다. 
            this.save(ipBanList); // db에 저장
        }
    }

    public boolean isIpBan(HttpServletRequest request){ // 이미 차단한 ip인지 판별하는 메서드
        String ipAddress = this.getIpAddress(request); // ip 주소 가져오기
        return ipBanRepository.findByIp(ipAddress)!=null ; // 이미 차단된 ip인지 아닌지 판별
    }

    public String getIpAddress(HttpServletRequest request){ // request에서 client의 ip주소 찾아서 반환하는 메서드
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
