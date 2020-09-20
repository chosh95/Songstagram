package com.cho.songstagram.makeComponent;

import com.cho.songstagram.domain.*;
import org.springframework.stereotype.Component;

@Component
public class MakeComponent {

    public Users makeUsers(){
        return Users.builder()
                .email("abc@abc.com")
                .password("1234")
                .name("kim")
                .build();
    }

    public Users makeUsers(String email){
        return Users.builder()
                .email(email)
                .password("1234")
                .name("kim")
                .build();
    }

    public Posts makePosts(Users users){
        return Posts.builder()
                .content("글내용")
                .singer("가수")
                .songName("곡제목")
                .picture("사진 경로")
                .users(users)
                .build();
    }

    public Follow makeFollow(Users from, Users to){
        return Follow.builder()
                .from(from)
                .to(to)
                .build();
    }

    public Likes makeLikes(Posts posts, Users users){
        return Likes.builder()
                .posts(posts)
                .users(users)
                .build();
    }

    public Comments makeComments(Users users, Posts posts){
        return Comments.builder()
                .users(users)
                .posts(posts)
                .content("content")
                .build();
    }

}
