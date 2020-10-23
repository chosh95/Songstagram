package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Users;
import com.cho.songstagram.makeComponent.MakeComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UsersRepositoryTest {

    @Autowired UsersRepository usersRepository;
    MakeComponent makeComponent = new MakeComponent();

    @Test
    public void 회원_저장(){
        Users users = makeComponent.makeUsers();

        usersRepository.save(users);

        assertEquals(users,usersRepository.findById(users.getId()).orElseGet(Users::new));
    }

    @Test
    public void 이메일로_회원찾기(){
        Users users = makeComponent.makeUsers();

        usersRepository.save(users);

        assertEquals(users,usersRepository.findByEmail(users.getEmail()).orElseGet(Users::new));
    }
}