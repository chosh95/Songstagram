package com.cho.songstagram.dto;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.makeComponent.MakeComponent;
import com.cho.songstagram.repository.CommentsRepository;
import com.cho.songstagram.service.CommentsService;
import com.cho.songstagram.service.PostsService;
import com.cho.songstagram.service.UsersService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CommentDtoTest {

    @Autowired UsersService usersService;
    @Autowired PostsService postsService;
    @Autowired CommentsService commentsService;
    @Autowired CommentsRepository commentsRepository;
    @Autowired MakeComponent makeComponent;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void NotNull_Test(){
        CommentDto commentDto = CommentDto.builder().build();
        Set<ConstraintViolation<CommentDto>> validate = validator.validate(commentDto);
        assertFalse(validate.isEmpty());
    }

    @Test
    public void CommentDto_Test(){
        CommentDto commentDto = CommentDto.builder()
                .commentId(1L)
                .comment("content")
                .createdDate("createdDate")
                .userId(2L)
                .userName("kim")
                .userPicture("picture")
                .build();

        assertAll(() -> { assertEquals(commentDto.getComment(),"content");},
                () -> {assertEquals(commentDto.getCommentId(),1L);},
                () -> {assertEquals(commentDto.getCreatedDate(),"createdDate");},
                () -> {assertEquals(commentDto.getUserId(),2L);},
                () -> {assertEquals(commentDto.getUserName(),"kim");},
                () -> {assertEquals(commentDto.getUserPicture(),"picture");});
    }
}