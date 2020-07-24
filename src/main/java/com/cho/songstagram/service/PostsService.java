package com.cho.songstagram.service;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.PostDto;
import com.cho.songstagram.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsService {

    private final PostsRepository postsRepository;
    private final LikesService likesService;

    @Transactional
    public void save(Posts posts){
        postsRepository.save(posts);
    }

    @Transactional
    public void delete(Posts posts){
        postsRepository.delete(posts);
    }

    public Optional<Posts> findById(Long id){
        return postsRepository.findById(id);
    }

    public Long getPostsCount(){
        return postsRepository.count();
    }

    public PostDto convertToDto(Posts posts){
        return PostDto.builder()
                .postId(posts.getId())
                .singer(posts.getSinger())
                .songName(posts.getSongName())
                .content(posts.getContent())
                .picture(posts.getPicture())
                .createdDate(posts.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .userId(posts.getUsers().getId())
                .userName(posts.getUsers().getName())
                .userPicture(posts.getUsers().getPicture())
                .likeIdList(likesService.findLikeIdList(posts))
                .build();
    }

    public List<PostDto> getPostList(int page, int contentPageCnt) {
        Page<Posts> posts = postsRepository.findAll(PageRequest.of(page - 1, contentPageCnt, Sort.by("createdDate").descending()));
        List<Posts> postsList = posts.getContent();
        List<PostDto> dtoList = new ArrayList<>();
        for (Posts post : postsList) {
            dtoList.add(this.convertToDto(post));
        }
        return dtoList;
    }

    public List<PostDto> getUserPostList(Users users, int page, int contentPageCnt){
        Page<Posts> posts = postsRepository.findAllByUsers(users, PageRequest.of(page-1,contentPageCnt,Sort.by("createdDate").descending()));
        List<Posts> postsList = posts.getContent();
        List<PostDto> dtoList = new ArrayList<>();
        for (Posts post : postsList) {
            dtoList.add(this.convertToDto(post));
        }
        return dtoList;
    }

}
