package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;
    @Autowired
    UsersRepository usersRepository;

    @Test
    public void 작성한_게시글_목록_페이징() throws InterruptedException {
        Users users = makeUser();
        usersRepository.save(users);

        List<Posts> postsList = new ArrayList<>();
        for(int i=0;i<10;i++){
            Thread.sleep(20); // 생성 시간 차이를 확실히 내기 위해 시간 지연
            Posts posts = makePosts(users);
            postsList.add(posts);
            postsRepository.save(posts);
        }

        //user가 작성한 0 페이지의 5개 글을 가져온다.
        Page<Posts> postsPage = postsRepository.findAllByUsers(users, PageRequest.of(0, 5, Sort.by("createdDate").descending()));

        //작성한 총 글의 수는 10개
        assertEquals(10,postsPage.getTotalElements());

        // list를 작성한 시간에 따라 내림차순으로 정렬
        postsList.sort((Posts p1, Posts p2) -> p2.getCreatedDate().compareTo(p1.getCreatedDate()));

        for(int i=0;i<5;i++){
            assertEquals(postsList.get(i),postsPage.getContent().get(i));
        }
    }

    public Users makeUser(){
        return Users.builder()
                .email("abc@abc.com")
                .password("1234")
                .name("kim")
                .build();
    }

    public Posts makePosts(Users users){
        return Posts.builder()
                .content("글내용")
                .singer("가수")
                .songName("곡제목")
                .picture("사진 경로")
                .users(users)
                .build();
    }
}