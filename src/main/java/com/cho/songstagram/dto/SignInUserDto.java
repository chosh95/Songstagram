package com.cho.songstagram.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignInUserDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    private String email;

    @Size(min=3,max=15,message = "최소 3글자에서 최대 15글자까지 가능합니다.")
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    private String password2;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min=2,max=8,message = "이름은 최소 2글자에서 8글자까지 가능합니다.")
    private String name;

    private String picture;

    public boolean matchPassword(){
        return this.password.equals(this.password2);
    }
}
