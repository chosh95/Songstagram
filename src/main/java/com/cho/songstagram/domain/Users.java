package com.cho.songstagram.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class Users {

    @Id @GeneratedValue
    @Column(name = "users_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "users")
    private List<Posts> postsList;

}
