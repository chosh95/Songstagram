package com.cho.songstagram.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PostDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void PostDto_Test(){
        List<Long> likeList = new ArrayList<>();
        likeList.add(1L);

        PostDto postDto = PostDto.builder()
                .userPicture("picture")
                .userName("name")
                .userId(1L)
                .createdDate("createdDate")
                .picture("picture")
                .content("content")
                .songName("songName")
                .singer("singer")
                .postId(2L)
                .likeUserIdList(likeList)
                .build();

        assertEquals(postDto.getUserPicture(),"picture");
        assertEquals(postDto.getUserName(),"name");
        assertEquals(postDto.getUserId(),1L);
        assertEquals(postDto.getCreatedDate(),"createdDate");
        assertEquals(postDto.getPicture(),"picture");
        assertEquals(postDto.getContent(),"content");
        assertEquals(postDto.getSongName(),"songName");
        assertEquals(postDto.getSinger(),"singer");
        assertEquals(postDto.getPostId(),2L);
        assertEquals(postDto.getLikeUserIdList(),likeList);
    }

    @Test
    public void singer_notBlank(){
        PostDto postDto = PostDto.builder()
                .songName("songName")
                .content("content")
                .build();

        Set<ConstraintViolation<PostDto>> validate = validator.validate(postDto);
        assertFalse(validate.isEmpty());
    }

    @Test
    public void songName_notBlank(){
        PostDto postDto = PostDto.builder()
                .singer("singer")
                .content("content")
                .build();

        Set<ConstraintViolation<PostDto>> validate = validator.validate(postDto);
        assertFalse(validate.isEmpty());
    }

    @Test
    public void content_notBlank(){
        PostDto postDto = PostDto.builder()
                .songName("songName")
                .singer("singer")
                .build();

        Set<ConstraintViolation<PostDto>> validate = validator.validate(postDto);
        assertFalse(validate.isEmpty());
    }
}