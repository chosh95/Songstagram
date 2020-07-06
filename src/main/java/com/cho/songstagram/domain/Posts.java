package com.cho.songstagram.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class Posts extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "posts_id")
    private Long id;

    private String singer;
    private String songName;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

//    @OneToOne(mappedBy = "posts", fetch = FetchType.LAZY)
//    private Likes likes;

    @OneToMany(mappedBy = "posts")
    private List<Comments> commentsList;

    @OneToOne(mappedBy = "posts", fetch = FetchType.LAZY)
    private ImageFile imageFile;

    @OneToMany(mappedBy = "posts")
    private List<Likes> likesList;
}
