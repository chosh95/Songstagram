package com.cho.songstagram.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

class ProfileUserDtoTest {

    @Test
    public void ProfileUserDto_Test(){
        ProfileUserDto profileUserDto = new ProfileUserDto();
        profileUserDto.setUserId(1L);
        profileUserDto.setUserName("name");
        profileUserDto.setUserPicture("picture");

        assertEquals(profileUserDto.getUserId(),1L);
        assertEquals(profileUserDto.getUserName(),"name");
        assertEquals(profileUserDto.getUserPicture(),"picture");
    }

}