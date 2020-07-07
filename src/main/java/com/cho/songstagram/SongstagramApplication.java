package com.cho.songstagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.Resource;

@SpringBootApplication
@EnableJpaAuditing
public class SongstagramApplication {

    public static void main(String[] args) {
        SpringApplication.run(SongstagramApplication.class, args);
    }

}
