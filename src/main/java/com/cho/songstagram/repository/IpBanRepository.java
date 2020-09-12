package com.cho.songstagram.repository;

import com.cho.songstagram.domain.IpBanList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IpBanRepository extends JpaRepository<IpBanList,Long> {
}
