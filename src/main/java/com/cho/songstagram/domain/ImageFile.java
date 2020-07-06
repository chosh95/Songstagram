package com.cho.songstagram.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ImageFile {

    @Id @GeneratedValue
    @Column(name = "imageFile_id")
    private Long id;

    private String fileName;
    private String fileType;
    private byte[] data;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="imageFile_id")
    private Posts posts;
}
