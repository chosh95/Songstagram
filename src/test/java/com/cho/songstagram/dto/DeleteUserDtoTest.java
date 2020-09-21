package com.cho.songstagram.dto;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DeleteUserDtoTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void 비밀번호_테스트(){
        DeleteUserDto deleteUserDto = new DeleteUserDto();
        deleteUserDto.setPassword("123");
        assertEquals("123",deleteUserDto.getPassword());
    }

    @Test
    public void 비밀번호_길이_짧을때_validation(){
        DeleteUserDto deleteUserDto = new DeleteUserDto();
        deleteUserDto.setPassword("12");
        Set<ConstraintViolation<DeleteUserDto>> validate = validator.validate(deleteUserDto);
        assertFalse(validate.isEmpty());
    }

    @Test
    public void 비밀번호_길이_길때_validation(){
        DeleteUserDto deleteUserDto = new DeleteUserDto();
        deleteUserDto.setPassword("1234123412341234");
        Set<ConstraintViolation<DeleteUserDto>> validate = validator.validate(deleteUserDto);
        assertFalse(validate.isEmpty());
    }

    @Test
    public void NotBlack_validation(){
        DeleteUserDto deleteUserDto = new DeleteUserDto();
        Set<ConstraintViolation<DeleteUserDto>> validate = validator.validate(deleteUserDto);
        assertFalse(validate.isEmpty());
    }
}