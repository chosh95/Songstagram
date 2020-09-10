package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {

    List<Comments> findCommentsByPosts(Posts posts); // 게시글에 작성한 댓글들 가져오기
}
