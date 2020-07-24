package com.cho.songstagram.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProfileUserDto {

    Long userId;
    String userName;
    String userPicture;

    public ProfileUserDto(Long userId, String userName, String userPicture) {
        this.userId = userId;
        this.userName = userName;
        this.userPicture = userPicture;
    }
}
