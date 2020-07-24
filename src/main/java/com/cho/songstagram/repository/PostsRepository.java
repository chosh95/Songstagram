package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends PagingAndSortingRepository<Posts, Long> {

    Page<Posts> findAllByUsers(Users users, Pageable pageable);
}
