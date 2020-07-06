package com.cho.songstagram.domain;

import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
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

    @OneToMany(mappedBy = "posts")
    private List<Comments> commentsList;

    @OneToOne(mappedBy = "posts", fetch = FetchType.LAZY)
    private ImageFile imageFile;

    @OneToMany(mappedBy = "posts")
    private List<Likes> likesList;

    @Builder
    public Posts(String singer, String songName, String content, Users users, ImageFile imageFile){
        this.singer = singer;
        this.songName = songName;
        this.content = content;
        this.users = users;
        this.imageFile = imageFile;
        likesList = null;
        commentsList = null;
    }
}
