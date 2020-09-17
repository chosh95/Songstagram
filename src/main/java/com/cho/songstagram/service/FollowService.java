package com.cho.songstagram.service;

import com.cho.songstagram.domain.Follow;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.FollowListDto;
import com.cho.songstagram.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;

    // 누가 누구 팔로우 했는지 follow 생성 후 db에 저장
    @Transactional
    public void save(Users from, Users to){
        if(isFollowing(from,to)) // 이미 팔로잉 중인 경우 예외 발생
            throw new IllegalStateException("이미 팔로잉 중입니다.");

        Follow follow = Follow.builder() // 팔로우 객체를 생성한다.
                .from(from)
                .to(to)
                .build();

        followRepository.save(follow); // db에 저장
    }

    // 팔로우 정보 db에서 삭제
    @Transactional
    public void delete(Users from, Users to){
        Follow follow = followRepository.findByFromAndTo(from, to);
        followRepository.delete(follow);
    }

    // 팔로워 목록 반환
    public List<Users> getFollower(Long userId){
        return followRepository.getFollower(userId);
    }

    // 팔로잉 목록 반환
    public List<Users> getFollowing(Long userId){
        return followRepository.getFollowing(userId);
    }

    // 팔로워 수 반환
    public Long countFollower(Users users){
        return followRepository.countFollower(users.getId());
    }

    // 팔로잉 수 반환
    public Long countFollowing(Users users){
        return followRepository.countFollowing(users.getId());
    }

    // from 유저가 to 유저를 팔로잉 중인지 반환
    public boolean isFollowing(Users from, Users to){
        Follow follow = followRepository.findByFromAndTo(from, to);
        return follow != null;
    }

    // from 유저의 to 유저에 대한 팔로우 정보 dto
    public FollowListDto convertDto(Users from, Users to){
        return FollowListDto.builder()
                .picture(to.getPicture())
                .userId(to.getId())
                .userName(to.getName())
                .isFollow(this.isFollowing(from,to))
                .build();
    }
}
