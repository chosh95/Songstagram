package com.cho.songstagram.service;

import com.cho.songstagram.domain.Users;
import com.cho.songstagram.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersService {

    private final UsersRepository usersRepository;

    @Transactional
    public void save(Users user){
        checkDuplicate(user.getEmail());
        usersRepository.save(user); //db에 user 저장
    }

    @Transactional
    public void update(Users users){
        usersRepository.save(users);
    }

    @Transactional
    public void delete(Users users){
        usersRepository.delete(users); //db에서 user 삭제
    }

    public Optional<Users> findByEmail(String email){
        return usersRepository.findByEmail(email); // 이메일 중복 여부 확인
    }

    public Optional<Users> findById(Long id){
        return usersRepository.findById(id); // id로 User 찾기
    }

    public Optional<Users> findUserFetchById(Long id){
        return usersRepository.findUserFetchById(id);
    }

    private void checkDuplicate(String email){
        Optional<Users> byEmail = findByEmail(email);
        if(byEmail.isPresent()) throw new IllegalStateException("이미 존재하는 회원입니다.");
    }
}
