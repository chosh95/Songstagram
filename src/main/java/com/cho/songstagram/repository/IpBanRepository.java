package com.cho.songstagram.repository;

import com.cho.songstagram.domain.IpBanList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpBanRepository extends JpaRepository<IpBanList,Long> {

    IpBanList findByIp(String ip); // ip로 개체 찾아오기
}
