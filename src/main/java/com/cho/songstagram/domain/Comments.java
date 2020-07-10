package com.cho.songstagram.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Getter
public class Comments extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "comments_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id")
    private Posts posts;

    private String userName;

    @Builder
    public Comments(String content, Posts posts, String userName){
        this.content = content;
        this.posts = posts;
        this.userName = userName;
        posts.getCommentsList().add(this);
    }
}
