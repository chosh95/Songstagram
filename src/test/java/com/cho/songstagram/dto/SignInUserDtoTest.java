package com.cho.songstagram.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SignInUserDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void SignInUserDto_Test(){
        SignInUserDto signInUserDto = new SignInUserDto();
        signInUserDto.setEmail("email@email.com");
        signInUserDto.setName("name");
        signInUserDto.setPassword("password");
        signInUserDto.setPassword2("password");
        signInUserDto.setPicture("picture");

        assertEquals(signInUserDto.getEmail(),"email@email.com");
        assertEquals(signInUserDto.getName(),"name");
        assertEquals(signInUserDto.getPassword(),"password");
        assertEquals(signInUserDto.getPassword2(),"password");
        assertEquals(signInUserDto.getPicture(),"picture");
    }

    @Test
    public void password_matchcing(){
        SignInUserDto signInUserDto = new SignInUserDto();
        signInUserDto.setPassword("password");
        signInUserDto.setPassword2("password");

        assertTrue(signInUserDto.matchPassword());
    }

    @Test
    public void email_notBlank(){
        SignInUserDto signInUserDto = new SignInUserDto();
        signInUserDto.setName("name");
        signInUserDto.setPassword("password");

        Set<ConstraintViolation<SignInUserDto>> validate = validator.validate(signInUserDto);
        assertFalse(validate.isEmpty());
    }

    @Test
    public void email_email형식() {
        SignInUserDto signInUserDto = new SignInUserDto();
        signInUserDto.setEmail("notEmail");

        Set<ConstraintViolation<SignInUserDto>> validate = validator.validate(signInUserDto);
        assertFalse(validate.isEmpty());
    }
    @Test
    public void email_minSize(){
        SignInUserDto signInUserDto = new SignInUserDto();
        signInUserDto.setEmail("short");

        Set<ConstraintViolation<SignInUserDto>> validate = validator.validate(signInUserDto);
        assertFalse(validate.isEmpty());
    }
    @Test
    public void password_minSize(){
        SignInUserDto signInUserDto = new SignInUserDto();
        signInUserDto.setPassword("o");

        Set<ConstraintViolation<SignInUserDto>> validate = validator.validate(signInUserDto);
        assertFalse(validate.isEmpty());
    }
    @Test
    public void password_maxSize(){
        SignInUserDto signInUserDto = new SignInUserDto();
        signInUserDto.setPassword("long long long");

        Set<ConstraintViolation<SignInUserDto>> validate = validator.validate(signInUserDto);
        assertFalse(validate.isEmpty());
    }
    @Test
    public void password_notBlank(){
        SignInUserDto signInUserDto = new SignInUserDto();
        signInUserDto.setName("name");
        signInUserDto.setEmail("email@email.com");

        Set<ConstraintViolation<SignInUserDto>> validate = validator.validate(signInUserDto);
        assertFalse(validate.isEmpty());
    }
    @Test
    public void name_notBlank(){
        SignInUserDto signInUserDto = new SignInUserDto();
        signInUserDto.setEmail("email@email.com");
        signInUserDto.setPassword("password");

        Set<ConstraintViolation<SignInUserDto>> validate = validator.validate(signInUserDto);
        assertFalse(validate.isEmpty());
    }
    @Test
    public void name_minSize(){
        SignInUserDto signInUserDto = new SignInUserDto();
        signInUserDto.setName("o");

        Set<ConstraintViolation<SignInUserDto>> validate = validator.validate(signInUserDto);
        assertFalse(validate.isEmpty());
    }
    @Test
    public void name_maxSize(){
        SignInUserDto signInUserDto = new SignInUserDto();
        signInUserDto.setName("long long long name");

        Set<ConstraintViolation<SignInUserDto>> validate = validator.validate(signInUserDto);
        assertFalse(validate.isEmpty());
    }
}