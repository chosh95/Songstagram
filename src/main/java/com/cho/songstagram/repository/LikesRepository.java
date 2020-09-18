package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Likes;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByPostsAndUsers(Posts posts, Users users); // 게시글과 작성자로 좋아요 반환
    List<Likes> findByPosts(Posts posts); // 게시글에 누른 좋아요 목록 반환
}
