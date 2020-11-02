package com.cho.songstagram.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CommentDto {

    @NotNull(message = "내용을 입력해주세요")
    private String comment;

    private Long commentId;
    private Long userId;
    private String userName;
    private String userPicture;
    private String createdDate;

    @Builder
    public CommentDto(String comment, Long commentId, Long userId, String userName, String userPicture, String createdDate) {
        this.comment = comment;
        this.commentId = commentId;
        this.userId = userId;
        this.userName = userName;
        this.userPicture = userPicture;
        this.createdDate = createdDate;
    }
}
