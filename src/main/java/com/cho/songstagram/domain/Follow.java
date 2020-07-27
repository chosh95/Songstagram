package com.cho.songstagram.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Follow {

    @Id @GeneratedValue
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user")
    private Users from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="to_user")
    private Users to;

    @Builder
    Follow(Users from, Users to){
        this.from = from;
        this.to = to;
    }
}
