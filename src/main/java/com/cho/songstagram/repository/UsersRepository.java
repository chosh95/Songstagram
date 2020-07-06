package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
