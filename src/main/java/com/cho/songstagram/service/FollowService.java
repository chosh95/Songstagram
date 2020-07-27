package com.cho.songstagram.service;

import com.cho.songstagram.domain.Follow;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.repository.FollowRepository;
import com.cho.songstagram.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;

    @Transactional
    public void save(Users from, Users to){
        Follow follow = Follow.builder()
                .from(from)
                .to(to)
                .build();
        from.getFollowing().add(follow);
        to.getFollower().add(follow);
        followRepository.save(follow);
    }

    @Transactional
    public void delete(Users from, Users to){
        Follow follow = followRepository.findByFromAndTo(from, to);
        followRepository.delete(follow);
    }

    public Long countFollower(Users users){
        return followRepository.countFollower(users.getId());
    }

    public Long countFollowing(Users users){
        return followRepository.countFollowing(users.getId());
    }

    public boolean isFollowing(Users from, Users to){
        Follow follow = followRepository.findByFromAndTo(from, to);
        return follow != null;
    }
}
