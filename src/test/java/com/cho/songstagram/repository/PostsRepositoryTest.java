package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Likes;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.makeComponent.MakeComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class PostsRepositoryTest {

    @Autowired PostsRepository postsRepository;
    @Autowired UsersRepository usersRepository;
    @Autowired LikesRepository likesRepository;
    @Autowired MakeComponent makeComponent;

    @Test
    public void 작성한_게시글_목록_페이징() throws InterruptedException {
        Users users = makeComponent.makeUsers();
        usersRepository.save(users);

        List<Posts> postsList = new ArrayList<>();
        for(int i=0;i<10;i++){
            Thread.sleep(10); // 생성 시간 차이를 확실히 내기 위해 시간 지연
            Posts posts = makeComponent.makePosts(users);
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


    @Test
    public void 좋아요_누른_게시글_목록_페이징(){
        Users users = makeComponent.makeUsers();
        usersRepository.save(users);

        Posts post1 = makeComponent.makePosts(users);
        Posts post2 = makeComponent.makePosts(users);
        postsRepository.save(post1);
        postsRepository.save(post2);

        Likes like1 = makeComponent.makeLikes(post1, users);
        Likes like2 = makeComponent.makeLikes(post2, users);
        likesRepository.save(like1);
        likesRepository.save(like2);

        //user가 두 게시글에 좋아요를 누른 후, postsRepository에서 user가 좋아요 누른 게시글을 찾아온다.
        Page<Posts> likeListPageable = postsRepository.getLikeListPageable(users.getId(), PageRequest.of(0, 5));
        assertEquals(likeListPageable.getContent().get(0),post1);
        assertEquals(likeListPageable.getContent().get(1),post2);
    }

    @Test
    public void 목록에_있는_user가_작성한_게시글_목록_페이징(){
        Users users = makeComponent.makeUsers();
        usersRepository.save(users);
        Users users2 = makeComponent.makeUsers();
        usersRepository.save(users2);

        Posts post1 = makeComponent.makePosts(users);
        postsRepository.save(post1);
        Posts post2 = makeComponent.makePosts(users2);
        postsRepository.save(post2);

        List<Users> userList = new ArrayList<>();
        userList.add(users);
        userList.add(users2);

        //user 목록에서 user가 작성한 게시글 목록을 찾아온다.
        Page<Posts> postsByUsers = postsRepository.getPostsCntByUsersList(userList, PageRequest.of(0, 2));
        assertEquals(postsByUsers.getContent().get(0),post1);
        assertEquals(postsByUsers.getContent().get(1),post2);
    }

    @Test
    public void 목록에_있는_유저가_작성한_게시글의_수(){
        Users users = makeComponent.makeUsers();
        usersRepository.save(users);
        Users users2 = makeComponent.makeUsers();
        usersRepository.save(users2);

        Posts post1 = makeComponent.makePosts(users);
        postsRepository.save(post1);
        Posts post2 = makeComponent.makePosts(users);
        postsRepository.save(post2);
        Posts post3 = makeComponent.makePosts(users2);
        postsRepository.save(post3);

        List<Users> usersList = new ArrayList<>();
        usersList.add(users);
        usersList.add(users2);

        Long postsCntByUsersList = postsRepository.getPostsCntByUsersList(usersList);
        assertEquals(postsCntByUsersList,3L);
    }

    @Test
    public void 유저가_작성한_게시글의_수(){
        Users users = makeComponent.makeUsers();
        usersRepository.save(users);

        Posts post1 = makeComponent.makePosts(users);
        postsRepository.save(post1);
        Posts post2 = makeComponent.makePosts(users);
        postsRepository.save(post2);
        Posts post3 = makeComponent.makePosts(users);
        postsRepository.save(post3);

        Long postsCntByUser = postsRepository.getPostsCntByUser(users);
        assertEquals(postsCntByUser,3L);
    }

    @Test
    public void 유저가_오늘_작성한_게시글의_수(){
        Users users = makeComponent.makeUsers();
        usersRepository.save(users);

        Posts post1 = makeComponent.makePosts(users);
        postsRepository.save(post1);
        Posts post2 = makeComponent.makePosts(users);
        postsRepository.save(post2);
        Posts post3 = makeComponent.makePosts(users);
        postsRepository.save(post3);

        Long postsCntByUserToday = postsRepository.getPostsCntByUserToday(users, LocalDate.now());
        assertEquals(postsCntByUserToday,3L);
    }
}