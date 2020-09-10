package com.cho.songstagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.Resource;

@SpringBootApplication
@EnableJpaAuditing //Auditing 기능 활성화
public class SongstagramApplication {

    public static void main(String[] args) {
        SpringApplication.run(SongstagramApplication.class, args);
    }

}
