package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

    Page<Posts> findAllByUsers(Users users, Pageable pageable); // User가 작성한 게시글 목록 pageable로 페이징 후 반환

    @Query(value = "SELECT l.posts FROM Likes l WHERE l.users.id = ?1",
        countQuery = "SELECT count(l.posts) FROM Likes l WHERE l.users.id = ?1")
    Page<Posts> getLikeListPageable(Long userId, Pageable pageable); // user가 좋아요 누른 게시글 목록 페이징 후 반환

    @Query(value = "SELECT p FROM Posts p WHERE p.users in ?1",
        countQuery = "SELECT count(p) FROM Posts p WHERE p.users in ?1")
    Page<Posts> getPostsByUsers(List<Users> users, Pageable pageable); // user 목록에 있는 user가 작성한 게시글 목록 페이징 후 반환

    @Query(value = "SELECT count(p) FROM Posts p WHERE p.users in ?1")
    Long getPostsByUsers(List<Users> users); // user 목록에 있는 user가 작성한 모든 게시글의 수 반환

    @Query(value = "SELECT count(p) FROM Posts p WHERE p.users = ?1")
    Long getPostsCntByUser(Users users); // user가 작성한 게시글 목록 반환

    @Query(value = "SELECT count(p) FROM Posts p WHERE p.users = ?1 and p.createDate = ?2")
    Long getPostsCntByUserToday(Users users, LocalDate today);
}
