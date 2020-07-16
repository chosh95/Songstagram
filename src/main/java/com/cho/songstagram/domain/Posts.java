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
    private String picture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> commentsList;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likesList;

    @Builder
    public Posts(String singer, String songName, String content, String picture, Users users){
        this.singer = singer;
        this.songName = songName;
        this.content = content;
        this.picture = picture;
        this.users = users;
        likesList = null;
        commentsList = null;
        this.users.getPostsList().add(this);
    }

    public void update(String singer, String songName, String content){
        this.singer = singer;
        this.songName = songName;
        this.content = content;
    }
}
