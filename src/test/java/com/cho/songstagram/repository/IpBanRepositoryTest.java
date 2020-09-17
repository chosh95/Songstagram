package com.cho.songstagram.repository;

import com.cho.songstagram.domain.IpBanList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class IpBanRepositoryTest {

    @Autowired
    IpBanRepository ipBanRepository;

    @Test
    public void IP로_객체_찾기(){
        IpBanList ipBanList = new IpBanList("127.0.0.1");

        ipBanRepository.save(ipBanList);

        assertEquals(ipBanList,ipBanRepository.findByIp("127.0.0.1"));
    }
}