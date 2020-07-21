package com.cho.songstagram.dto;

import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;

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
        pageCnt = contentCnt / contentPageCnt; //한 페이지당 글 5개씩 보여주기
        if (contentCnt % contentPageCnt != 0) pageCnt++;

        this.min = ((currentPage - 1) / contentPageCnt) * contentPageCnt + 1;
        this.max = min + paginationCnt - 1;
        if (this.max > pageCnt) max = pageCnt;

        prevPage = this.min - 1;
        nextPage = this.max + 1;
        if (nextPage > pageCnt) nextPage = pageCnt;

        System.out.println(min);
        System.out.println(max);
        this.numList = new int[max-min+1];
        for (int i = 0; i < numList.length; i++) {
            numList[i] = min + i;
        }
    }
}
