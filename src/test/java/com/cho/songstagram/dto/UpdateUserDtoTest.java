package com.cho.songstagram.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UpdateUserDtoTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void UpdateUserDto_Test() {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setPicture("picture");
        updateUserDto.setName("name");

        assertEquals(updateUserDto.getName(),"name");
        assertEquals(updateUserDto.getPicture(),"picture");
    }

    @Test
    public void name_notBlank(){
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setPicture("picture");

        Set<ConstraintViolation<UpdateUserDto>> validate = validator.validate(updateUserDto);
        assertFalse(validate.isEmpty());
    }

    @Test
    public void name_minSize(){
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setPicture("picture");
        updateUserDto.setName("n");

        Set<ConstraintViolation<UpdateUserDto>> validate = validator.validate(updateUserDto);
        assertFalse(validate.isEmpty());
    }

    @Test
    public void name_maxSize(){
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setPicture("picture");
        updateUserDto.setName("longlong_name");

        Set<ConstraintViolation<UpdateUserDto>> validate = validator.validate(updateUserDto);
        assertFalse(validate.isEmpty());
    }
}