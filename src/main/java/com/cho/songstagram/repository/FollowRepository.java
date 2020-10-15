package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Follow;
import com.cho.songstagram.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Follow findByFromAndTo(Users from, Users to); // 팔로워와 팔로잉 유저로 팔로우 정보 찾기

    @Query("SELECT f.from From Follow f WHERE f.to.id=:id")
    Set<Users> getFollower(@Param("id") Long userId); // id에 해당하는 User의 팔로워 목록 찾기

    @Query("SELECT f.to From Follow f WHERE f.from.id=:id")
    Set<Users> getFollowing(@Param("id") Long userId); // id에 해당하는 User의 팔로잉 목록 찾기

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.to.id=:id")
    Long countFollower(@Param("id") Long userId); // user의 팔로워 수 반환

    @Query("SELECT count(f) FROM Follow f WHERE f.from.id=:id")
    Long countFollowing(@Param("id") Long userId); // user의 팔로잉 수 반환
}
