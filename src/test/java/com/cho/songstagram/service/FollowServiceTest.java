package com.cho.songstagram.service;

import com.cho.songstagram.domain.Follow;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.makeComponent.MakeComponent;
import com.cho.songstagram.repository.FollowRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FollowServiceTest {

    @Autowired FollowService followService;
    @Autowired FollowRepository followRepository;
    @Autowired UsersService usersService;
    MakeComponent makeComponent = new MakeComponent();

    @Test
    public void 팔로우_정보_저장(){
        Users userA = makeComponent.makeUsers("aaa@aaa.com");
        usersService.save(userA);

        Users userB = makeComponent.makeUsers("bbb@bbb.com");
        usersService.save(userB);

        followService.save(userA,userB);

        assertNotNull(followRepository.findByFromAndTo(userA,userB));
    }

    @Test
    public void 팔로우_중복_확인(){
        Users userA = makeComponent.makeUsers("aaa@aaa.com");
        usersService.save(userA);

        Users userB = makeComponent.makeUsers("bbb@bbb.com");
        usersService.save(userB);

        followService.save(userA,userB);
        
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> followService.save(userA,userB),
                "팔로우 중복 확인에 실패했습니다.");
        
        assertTrue(exception.getMessage().contains("이미 팔로잉 중입니다."));
    }

    @Test
    public void 팔로우_삭제(){
        Users userA = makeComponent.makeUsers("aaa@aaa.com");
        usersService.save(userA);

        Users userB = makeComponent.makeUsers("bbb@bbb.com");
        usersService.save(userB);

        followService.save(userA,userB); // 팔로우 정보 저장하고
        
        followService.delete(userA,userB); // 삭제하면

        assertNull(followRepository.findByFromAndTo(userA,userB)); // 팔로우 정보가 null이 돼야 한다.
    }

    @Test
    public void 팔로우_여부_확인(){
        Users userA = makeComponent.makeUsers("aaa@aaa.com");
        usersService.save(userA);

        Users userB = makeComponent.makeUsers("bbb@bbb.com");
        usersService.save(userB);

        followService.save(userA,userB); //팔로우 정보 저장하면
        assertTrue(followService.isFollowing(userA,userB)); // 팔로잉 중이라고 뜨고

        followService.delete(userA,userB); // 팔로우 정보 삭제하면
        assertFalse(followService.isFollowing(userA,userB)); // 팔로잉 여부가 false가 돼야한다.
    }

}