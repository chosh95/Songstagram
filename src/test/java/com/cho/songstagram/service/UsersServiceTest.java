package com.cho.songstagram.service;

import com.cho.songstagram.domain.Users;
import com.cho.songstagram.makeComponent.MakeComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UsersServiceTest {

    @Autowired UsersService usersService;
    MakeComponent makeComponent = new MakeComponent();

    @Test
    public void 회원_중복시_오류가_발생한다(){
        Users user1 = makeComponent.makeUsers();
        Users user2 = makeComponent.makeUsers();

        usersService.save(user1);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> usersService.save(user2),
        "예외가 발생해야 하지만 발생하지 않았다.");
        
        assertTrue(exception.getMessage().contains("이미 존재하는 회원입니다."));
    }

    @Test
    public void 회원_삭제(){
        Users user1 = makeComponent.makeUsers();

        usersService.save(user1);
        assertTrue(usersService.findByEmail("abc@abc.com").isPresent());

        // 삭제 후엔 id로 찾아도 empty이다.
        usersService.delete(user1);
        assertEquals(usersService.findById(user1.getId()), Optional.empty());
    }

    @Test
    public void 회원_아이디로_찾기(){
        Users user1 = makeComponent.makeUsers();

        usersService.save(user1);

        assertEquals(user1,usersService.findById(user1.getId()).orElseGet(Users::new));
    }

    @Test
    public void 회원_이메일로_찾기(){
        Users user1 = makeComponent.makeUsers();

        usersService.save(user1);

        assertEquals(user1,usersService.findByEmail(user1.getEmail()).orElseGet(Users::new));
    }

}