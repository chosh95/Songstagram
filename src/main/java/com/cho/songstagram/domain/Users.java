package com.cho.songstagram.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Users {

    @Id @GeneratedValue
    @Column(name = "users_id")
    private Long id;

    @Column(nullable = false)
    private String name; // 회원 이름

    @Column(nullable = false)
    private String email; // 이메일 : 로그인 id로 사용

    private String picture; // 프로필 이미지 파일명

    @Column(nullable = false)
    private String password; // 비밀번호

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Posts> postsList; // 작성 게시글 목록

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comments> comemntsList; // 작성한 댓글 목록

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likesList; // 좋아요 목록

    @OneToMany(mappedBy = "from", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> following; // 팔로잉 중인 사람

    @OneToMany(mappedBy = "to", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> follower; // 팔로워 목록

    @Builder
    public Users(String name, String email, String password, String picture){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.password = password;
        postsList = null;
        comemntsList = null;
        likesList = null;
        follower = null;
        following = null;
    }

    public void updatePicture(String picture){
        this.picture = picture;
    }

    public void updateName(String name){
        this.name = name;
    }

}
