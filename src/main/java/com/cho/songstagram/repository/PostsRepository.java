package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

    Page<Posts> findAllByUsers(Users users, Pageable pageable);

    @Query(value = "SELECT l.posts FROM Likes l WHERE l.users.id = ?1",
        countQuery = "SELECT count(l.posts) FROM Likes l WHERE l.users.id = ?1")
    Page<Posts> getLikeListPageable(Long userId,Pageable pageable);

    @Query(value = "SELECT p FROM Posts p WHERE p.users in ?1",
        countQuery = "SELECT count(p) FROM Posts p WHERE p.users in ?1")
    Page<Posts> getPostsByUsers(List<Users> users, Pageable pageable);

    @Query(value = "SELECT count(p) FROM Posts p WHERE p.users in ?1")
    Long getPostsByUsers(List<Users> users);
}
