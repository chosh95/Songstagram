package com.cho.songstagram.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comments extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "comments_id")
    private Long id;

    private String content; // 댓글 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id")
    private Posts posts; // 댓글 작성된 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users; // 댓글 작성자

    @Builder
    public Comments(String content, Posts posts, Users users){
        this.content = content;
        this.posts = posts;
        this.users = users;
    }
}
