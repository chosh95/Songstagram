package com.cho.songstagram.service;

import com.cho.songstagram.domain.Users;
import com.cho.songstagram.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UsersService {

    @Autowired
    UsersRepository usersRepository;

    public void addUser(Users user){
        usersRepository.save(user);
    }

    public List<Users> getAllUsers(){
        List<Users> usersList = usersRepository.findAll();
        return usersList;
    }

}
