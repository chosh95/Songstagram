package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {

    List<Comments> findCommentsByPosts(Posts posts); // 게시글에 작성한 댓글들 가져오기

    // fetch join을 활용해 댓글을 작성한 user 정보까지 쿼리 한 번으로 해결한다.
    @Query("select c from Comments c join fetch c.users where c.posts = ?1")
    Set<Comments> findCommentsAndUsersByPosts(Posts posts);
}
