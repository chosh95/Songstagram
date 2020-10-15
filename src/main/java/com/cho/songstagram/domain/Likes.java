package com.cho.songstagram.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Likes {

    @Id @GeneratedValue
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id")
    private Posts posts; // 좋아요 누른 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users; // 좋아요 누른 사용자

    @Builder
    public Likes(Posts posts, Users users){
        this.posts = posts;
        this.users = users;
    }
}
