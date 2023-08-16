package com.intuit.funding.service;

import com.intuit.funding.dao.UserRepository;
import com.intuit.funding.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService{

    @Autowired
    private UserRepository userRepository;

    //TEST
    public User getUserDetails(String email){
        return userRepository.getUserByEmail(email);
    }


    //TEST
    public User saveUser(User user){
        return userRepository.save(user);
    }
}
