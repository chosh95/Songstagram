package com.cho.songstagram.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PostDto {

    @NotBlank(message = "가수명을 입력해주세요")
    private String singer;
    @NotBlank(message = "곡명을 입력해주세요")
    private String songName;
    @NotBlank(message = "내용을 입력해주세요")
    private String content;
    private String picture;
    private Long userId;
}
