package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Users;
import com.cho.songstagram.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.security.RunAs;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UsersRepositoryTest {

    @Autowired UsersRepository usersRepository;

    @Test
    public void 회원_저장(){
        Users users = Users.builder()
                .name("kim")
                .email("axd@axd.com")
                .password("1234")
                .build();

        usersRepository.save(users);
        assertEquals(users,usersRepository.findById(users.getId()).orElse(new Users()));
    }

    @Test
    public void 이메일로_회원찾기(){
        Users users = Users.builder()
                .name("kim")
                .email("axd@axd.com")
                .password("1234")
                .build();
        
        usersRepository.save(users);

        assertEquals(users,usersRepository.findByEmail(users.getEmail()).orElse(new Users()));
    }
}