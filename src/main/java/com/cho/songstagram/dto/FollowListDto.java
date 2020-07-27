package com.cho.songstagram.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FollowListDto {

    private String picture;
    private String userName;
    private Long userId;
    private boolean isFollow;

    @Builder
    public FollowListDto(String picture, String userName, Long userId, boolean isFollow) {
        this.picture = picture;
        this.userName = userName;
        this.userId = userId;
        this.isFollow = isFollow;
    }
}
