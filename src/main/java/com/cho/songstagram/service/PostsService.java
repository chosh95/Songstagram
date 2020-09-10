package com.cho.songstagram.service;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.PostDto;
import com.cho.songstagram.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
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
    private final FollowService followService;

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


    //index 페이지용 모든 게시글 목록
    public List<PostDto> getPostList(int page, int contentPageCnt) {
        Page<Posts> posts = postsRepository.findAll(PageRequest.of(page - 1, contentPageCnt, Sort.by("createdDate").descending()));
        List<Posts> postsList = posts.getContent();
        List<PostDto> dtoList = new ArrayList<>();
        for (Posts post : postsList) {
            dtoList.add(this.convertToDto(post));
        }
        return dtoList;
    }

    //유저가 작성한 게시글 목록
    public List<PostDto> getUserPostList(Users users, int page, int contentPageCnt){
        Page<Posts> posts = postsRepository.findAllByUsers(users, PageRequest.of(page-1,contentPageCnt,Sort.by("createdDate").descending()));
        List<Posts> postsList = posts.getContent();
        List<PostDto> dtoList = new ArrayList<>();
        for (Posts post : postsList) {
            dtoList.add(this.convertToDto(post));
        }
        return dtoList;
    }

    //유저가 좋아요 누른 게시글 목록
    public List<PostDto> getUserLikeListPage(Long userId, int page, int contentPageCnt){
        Page<Posts> posts = postsRepository.getLikeListPageable(userId,PageRequest.of(page-1,contentPageCnt,Sort.by("posts.createdDate").descending()));
        List<Posts> content = posts.getContent();
        List<PostDto> dtoList = new ArrayList<>();
        for (Posts posts1 : content) {
            dtoList.add(this.convertToDto(posts1));
        }
        return dtoList;
    }

    //팔로우 한 사람들의 게시글 목록
    public List<PostDto> getFollowListPage(Long userId, int page, int contentPageCnt){
        List<Users> users = followService.getFollowing(userId);
        Page<Posts> posts = postsRepository.getPostsByUsers(users, PageRequest.of(page - 1, contentPageCnt, Sort.by("createdDate").descending()));
        List<Posts> content = posts.getContent();
        List<PostDto> dtoList = new ArrayList<>();
        for (Posts posts1 : content) {
            dtoList.add(this.convertToDto(posts1));
        }
        return dtoList;
    }

    public Long getFollowPostCount(Long userId){
        List<Users> users = followService.getFollowing(userId);
        return postsRepository.getPostsByUsers(users);
    }

    // 유저가 작성한 총 게시글 수 구하는 함수
    public Long getPostsCntByUser(Users users){
        return postsRepository.getPostsCntByUser(users);
    }

    public Posts makePost(PostDto postDto, Users user, String picture){
        return Posts.builder() // 게시글 생성
                .singer(postDto.getSinger())
                .songName(postDto.getSongName())
                .content(postDto.getContent())
                .picture(picture)
                .users(user)
                .build();
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
}
