package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Posts;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends PagingAndSortingRepository<Posts, Long> {

}
