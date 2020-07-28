package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends PagingAndSortingRepository<Posts, Long> {

    Page<Posts> findAllByUsers(Users users, Pageable pageable);

    @Query(value = "SELECT l.posts FROM Likes l WHERE l.users.id = ?1",
        countQuery = "SELECT count(l.posts) FROM Likes l WHERE l.users.id = ?1")
    Page<Posts> getLikeListPageable(Long userId,Pageable pageable);
}
