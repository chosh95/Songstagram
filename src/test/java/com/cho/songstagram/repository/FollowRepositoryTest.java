package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Follow;
import com.cho.songstagram.domain.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FollowRepositoryTest {

    @Autowired FollowRepository followRepository;
    @Autowired UsersRepository usersRepository;

    @Test
    public void 유저_정보로_팔로우_찾기(){
        Users userA = makeUsers("aaa@aaa.com");
        usersRepository.save(userA);

        Users userB = makeUsers("bbb@bb.com");
        usersRepository.save(userB);

        Follow follow = makeFollow(userA, userB);
        followRepository.save(follow);

        assertEquals(follow,followRepository.findByFromAndTo(userA,userB));
    }

    @Test
    public void 유저_팔로워_목록_찾기(){
        Users userA = makeUsers("aaa@aaa.com");
        usersRepository.save(userA);

        Users userB = makeUsers("bbb@bbb.com");
        usersRepository.save(userB);

        Users userC = makeUsers("ccc@ccc.com");
        usersRepository.save(userC);

        Follow follow = makeFollow(userA, userC);
        followRepository.save(follow);
        Follow follow2 = makeFollow(userB, userC);
        followRepository.save(follow2);

        List<Users> followIdList = new ArrayList<>();
        followIdList.add(userA);
        followIdList.add(userB);

        assertEquals(followIdList,followRepository.getFollower(userC.getId()));
    }

    @Test
    public void 유저_팔로잉_목록_찾기(){
        Users userA = makeUsers("aaa@aaa.com");
        usersRepository.save(userA);

        Users userB = makeUsers("bbb@bbb.com");
        usersRepository.save(userB);

        Users userC = makeUsers("ccc@ccc.com");
        usersRepository.save(userC);

        Follow follow = makeFollow(userA, userB);
        followRepository.save(follow);
        Follow follow2 = makeFollow(userA, userC);
        followRepository.save(follow2);

        List<Users> followIdList = new ArrayList<>();
        followIdList.add(userB);
        followIdList.add(userC);

        assertEquals(followIdList,followRepository.getFollowing(userA.getId()));
    }

    @Test
    public void 유저_팔로워_수_찾기(){
        Users userA = makeUsers("aaa@aaa.com");
        usersRepository.save(userA);

        Users userB = makeUsers("bbb@bbb.com");
        usersRepository.save(userB);

        Users userC = makeUsers("ccc@ccc.com");
        usersRepository.save(userC);

        Follow follow = makeFollow(userA, userC);
        followRepository.save(follow);
        Follow follow2 = makeFollow(userB, userC);
        followRepository.save(follow2);

        assertEquals(2L,followRepository.countFollower(userC.getId()));
    }

    @Test
    public void 유저_팔로잉_수_찾기(){
        Users userA = makeUsers("aaa@aaa.com");
        usersRepository.save(userA);

        Users userB = makeUsers("bbb@bbb.com");
        usersRepository.save(userB);

        Users userC = makeUsers("ccc@ccc.com");
        usersRepository.save(userC);

        Follow follow = makeFollow(userA, userB);
        followRepository.save(follow);
        Follow follow2 = makeFollow(userA, userC);
        followRepository.save(follow2);

        assertEquals(2L,followRepository.countFollowing(userA.getId()));
    }

    public Follow makeFollow(Users from, Users to){
        return Follow.builder()
                .from(from)
                .to(to)
                .build();
    }

    public Users makeUsers(String email){
        return Users.builder()
                .name("name")
                .password("password")
                .picture("picture")
                .email(email)
                .build();
    }
}