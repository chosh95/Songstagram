package com.cho.songstagram.domain.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class UserDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    @UniqueElements
    private String email;

    @Size(min=3,max=15,message = "최소 3글자에서 최대 15글자까지 가능합니다.")
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 3, max = 10, message = "이름은 3자에서 10자까지 가능합니다.")
    private String name;

    private String picture;
}
