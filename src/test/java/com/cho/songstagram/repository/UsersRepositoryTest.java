package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class UsersRepositoryTest {

    @Autowired UsersRepository usersRepository;

    @Test
    public void 회원_저장(){
        Users users = makeUser();

        usersRepository.save(users);

        assertEquals(users,usersRepository.findById(users.getId()).orElse(new Users()));
    }

    @Test
    public void 이메일로_회원찾기(){
        Users users = makeUser();

        usersRepository.save(users);

        assertEquals(users,usersRepository.findByEmail(users.getEmail()).orElse(new Users()));
    }

    public Users makeUser(){
        return Users.builder()
                .email("abc@abc.com")
                .password("1234")
                .name("kim")
                .build();
    }
}