package com.cho.songstagram.dto;

import lombok.Data;

@Data
public class PageDto {

    private int min; //최소 페이지 번호
    private int max; //최대 페이지 번호

    private int currentPage; // 현재 페이지 번호
    private int pageCnt; //총 페이지 수

    private int prevPage; // 이전 페이지 번호
    private int nextPage; // 다음 페이지 번호
    private int[] numList; // 하단 버튼 리스트

    //paginationCnt : 하단 페이지 버튼의 개수, contentCnt : 전체 글의 개수, contentPageCnt : 페이지 당 글의 개수
    public PageDto(int currentPage, int paginationCnt, int contentCnt, int contentPageCnt) {
        this.currentPage = currentPage;
        pageCnt = contentCnt / contentPageCnt; // 총 몇 페이지가 되는지 계산
        if (contentCnt % contentPageCnt != 0) pageCnt++; // 나머지 0 아니면 1페이지 추가. ex) 13 / 5 면 3 페이지 필요

        this.min = ((currentPage - 1) / contentPageCnt) * contentPageCnt + 1; 
        this.max = min + paginationCnt - 1;
        if (this.max > pageCnt) max = pageCnt; // 최대 페이지 조절

        prevPage = this.min - 1;
        nextPage = this.max + 1;
        if (nextPage > pageCnt) nextPage = pageCnt;

        this.numList = new int[max-min+1]; // 하단 페이지 번호 리스트
        for (int i = 0; i < numList.length; i++)
            numList[i] = min + i;
    }
}
