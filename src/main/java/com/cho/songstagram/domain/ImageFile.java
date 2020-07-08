package com.cho.songstagram.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ImageFile {

    @Id @GeneratedValue
    @Column(name = "imageFile_id")
    private String id;

    private String fileName;
    private String fileOriName;
    private String fileUrl;

    @OneToOne
    private Posts posts;
}
