package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Follow;
import com.cho.songstagram.domain.Users;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Follow findByFromAndTo(Users from, Users to);

    @Query("SELECT f.from From Follow f WHERE f.to.id=:id")
    List<Users> getFollower(@Param("id") Long userId);

    @Query("SELECT f.to From Follow f WHERE f.from.id=:id")
    List<Users> getFollowing(@Param("id") Long userId);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.to.id=:id")
    Long countFollower(@Param("id") Long userId);

    @Query("SELECT count(f) FROM Follow f WHERE f.from.id=:id")
    Long countFollowing(@Param("id") Long userId);
}
