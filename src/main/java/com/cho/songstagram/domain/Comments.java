package com.cho.songstagram.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comments extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "comments_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id")
    private Posts posts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @Builder
    public Comments(String content, Posts posts, Users users){
        this.content = content;
        this.posts = posts;
        this.users = users;
        posts.getCommentsList().add(this);
        users.getComemntsList().add(this);
    }
}
