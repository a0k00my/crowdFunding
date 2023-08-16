package com.intuit.funding.config;

import com.intuit.funding.dao.UserRepository;
import com.intuit.funding.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("Could not found user email");
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        System.out.println("CD = "+customUserDetails);
        return customUserDetails;
    }
}
