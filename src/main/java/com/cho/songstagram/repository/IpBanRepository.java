package com.cho.songstagram.repository;

import com.cho.songstagram.domain.IpBanList;
import com.cho.songstagram.service.IpBanService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IpBanRepository extends JpaRepository<IpBanList,Long> {

    IpBanList findByIp(String ip);
}
