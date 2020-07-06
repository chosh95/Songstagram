package com.cho.songstagram.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseFileDto {

    String fileName;
    String fileUri;
    String fileType;
    Long fileSize;
}
