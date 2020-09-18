package com.cho.songstagram.service;

import com.cho.songstagram.domain.IpBanList;
import com.cho.songstagram.makeComponent.MakeComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class IpBanServiceTest {

    @Autowired IpBanService ipBanService;
    @Autowired MakeComponent makeComponent;

    @Test
    public void IP_저장(){
        IpBanList ipBanList = new IpBanList("127.0.0.1");

        ipBanService.save(ipBanList); // ip를 저장했을때

        assertTrue(ipBanService.isIpBan("127.0.0.1")); // ip를 이미 차단했다고 뜨는지 확인
    }

    @Test
    public void IP_중복_확인(){
        IpBanList ipBanList = new IpBanList("127.0.0.1");
        ipBanService.save(ipBanList);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> ipBanService.save(new IpBanList("127.0.0.1")),
                "ip 중복 확인에 실패했습니다.");

        assertTrue(exception.getMessage().contains("이미 차단된 ip입니다."));
    }

    @Test
    public void 요청으로_들어온_IP_차단하기(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");

        ipBanService.banIp(request);

        assertTrue(ipBanService.isIpBan("127.0.0.1"));
    }

    @Test
    public void IP_차단_확인(){
        IpBanList ipBanList = new IpBanList("127.0.0.1");

        ipBanService.save(ipBanList);

        assertTrue(ipBanService.isIpBan("127.0.0.1"));
    }

    @Test
    public void request에서_IP_추출하기(){
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setRemoteAddr("127.0.0.1");

        assertEquals("127.0.0.1",ipBanService.getIpAddress(request));
    }
}