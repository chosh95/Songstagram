package com.cho.songstagram.service;

import com.cho.songstagram.domain.Users;
import com.cho.songstagram.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersService {

    private final UsersRepository usersRepository;

    @Transactional
    public void addUser(Users user){
        usersRepository.save(user);
    }

    public List<Users> getAllUsers(){
        List<Users> usersList = usersRepository.findAll();
        return usersList;
    }

}
