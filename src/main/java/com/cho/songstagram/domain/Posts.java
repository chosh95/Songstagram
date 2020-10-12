package com.cho.songstagram.domain;

import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor
public class Posts extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "posts_id")
    private Long id;

    private String singer; //가수
    private String songName; //곡 제목

    @Column(length = 500)
    private String content; // 글 내용
    private String picture; // 사진 파일명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users; // 작성자

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comments> commentsList; // 게시글에 달린 댓글 목록

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Likes> likesList; // 게시글에 누른 좋아요 목록

    @Builder
    public Posts(String singer, String songName, String content, String picture, Users users){
        this.singer = singer;
        this.songName = songName;
        this.content = content;
        this.picture = picture;
        this.users = users;
        likesList = new HashSet<>();
        commentsList = new HashSet<>();
    }

    // 게시글 수정 기능시 사용
    public void update(String singer, String songName, String content){
        this.singer = singer;
        this.songName = songName;
        this.content = content;
    }
}
