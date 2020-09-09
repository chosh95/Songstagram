package com.cho.songstagram.service;

import com.cho.songstagram.domain.Users;
import com.cho.songstagram.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersService {

    private final UsersRepository usersRepository;

    @Transactional
    public void save(Users user){
        usersRepository.save(user);
    }

    @Transactional
    public void delete(Users users){
        usersRepository.delete(users);
    }

    public Optional<Users> findByEmail(String email){
        return usersRepository.findByEmail(email);
    }

    public Optional<Users> findById(Long id){ return usersRepository.findById(id);}

    public List<Users> getAllUsers(){
        List<Users> usersList = usersRepository.findAll();
        return usersList;
    }
}
