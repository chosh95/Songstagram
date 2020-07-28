package com.cho.songstagram.service;

import com.cho.songstagram.domain.Follow;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.FollowListDto;
import com.cho.songstagram.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final UsersService usersService;

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

    public List<Users> getFollower(Long userId){
        return followRepository.getFollower(userId);
    }

    public List<Users> getFollowing(Long userId){
        return followRepository.getFollowing(userId);
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

    public FollowListDto convertDto(Users from, Users to){
        return FollowListDto.builder()
                .picture(to.getPicture())
                .userId(to.getId())
                .userName(to.getName())
                .isFollow(this.isFollowing(from,to))
                .build();
    }
}
