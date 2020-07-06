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
    private String fileType;

    @Lob
    private byte[] data;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="imageFile_id")
    private Posts posts;

    public ImageFile(String fileName, String fileType, byte[] data){
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }
}
