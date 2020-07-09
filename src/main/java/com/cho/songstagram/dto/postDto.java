package com.cho.songstagram.dto;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Users;
import lombok.Data;

@Data
public class postDto {

    private String singer;
    private String songName;
    private String content;
    private Users users;
    private String picture;
}
