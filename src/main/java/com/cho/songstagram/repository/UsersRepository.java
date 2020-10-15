package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email); // 이메일 가진 user 반환

    @EntityGraph(attributePaths =  {"postsList", "follower", "following"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Users> findWithPostsById(Long id);
}
