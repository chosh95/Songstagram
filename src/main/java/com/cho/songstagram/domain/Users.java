package com.cho.songstagram.domain;

import com.sun.istack.Nullable;
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

    private String picture;
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "users")
    private List<Posts> postsList;


    @Builder
    public Users(String name, String email, String picture, Role role){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

    public Users update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }
}
