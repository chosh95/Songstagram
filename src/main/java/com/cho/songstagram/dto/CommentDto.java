package com.cho.songstagram.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CommentDto {

    @NotNull(message = "내용을 입력해주세요")
    String comment;
}
