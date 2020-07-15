package com.cho.songstagram.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class DeleteUserDto {

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min=3,max=15,message = "최소 3글자에서 최대 15글자까지 가능합니다.")
    private String password;

}
