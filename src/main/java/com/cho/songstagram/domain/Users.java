package com.cho.songstagram.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String name;

    @Column(nullable = false)
    private String email;

    private String picture;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Posts> postsList;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comments> comemntsList;

    @Builder
    public Users(String name, String email, String password, String picture){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.password = password;
        postsList = null;
        comemntsList = null;
    }

    public boolean matchPassword(String password){
        return this.password.equals(password);
    }

    public void updatePicture(String picture){
        this.picture = picture;
    }

    public void updateName(String name){
        this.name = name;
    }
}
