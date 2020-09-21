package com.cho.songstagram.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageDtoTest {

    @Test
    public void PageDto_Test(){
        PageDto pageDto = new PageDto(1,10,100,10);
        assertEquals(pageDto.getCurrentPage(),1);
        assertEquals(pageDto.getMax(),10);
        assertEquals(pageDto.getMin(),1);
        assertEquals(pageDto.getNextPage(),10);
        assertEquals(pageDto.getPrevPage(),0);
        assertEquals(pageDto.getPageCnt(),10);
        assertEquals(pageDto.getNumList().length,10);
    }
}