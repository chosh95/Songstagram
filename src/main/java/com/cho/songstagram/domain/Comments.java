package com.cho.songstagram.domain;

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
}
