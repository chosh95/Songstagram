package com.cho.songstagram.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FollowListDtoTest {

    @Test
    public void FollowListDto_Test(){
        FollowListDto followListDto = FollowListDto.builder()
                .isFollow(true)
                .picture("picture")
                .userId(1L)
                .userName("kim")
                .build();

        assertTrue(followListDto.isFollow());
        assertEquals(followListDto.getPicture(),"picture");
        assertEquals(followListDto.getUserId(),1L);
        assertEquals(followListDto.getUserName(),"kim");
    }
}