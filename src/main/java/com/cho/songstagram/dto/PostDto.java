package com.cho.songstagram.dto;

import com.cho.songstagram.domain.Users;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class PostDto {

    private Long postId;

    @NotBlank(message = "가수명을 입력해주세요")
    private String singer;

    @NotBlank(message = "곡명을 입력해주세요")
    private String songName;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    private String picture;

    private String createdDate;

    private Long userId;
    private String userName;
    private String userPicture;
    private Set<Long> likeUserIdList;

    @Builder
    public PostDto(Long postId, String singer, String songName, String content, String picture, String createdDate, Long userId, String userName, String userPicture, Set<Long> likeUserIdList) {
        this.postId = postId;
        this.singer = singer;
        this.songName = songName;
        this.content = content;
        this.picture = picture;
        this.createdDate = createdDate;
        this.userId = userId;
        this.userName = userName;
        this.userPicture = userPicture;
        this.likeUserIdList = likeUserIdList;
    }
}
