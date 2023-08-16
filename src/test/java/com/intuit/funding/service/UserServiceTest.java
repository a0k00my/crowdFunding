package com.intuit.funding.service;

import com.intuit.funding.dao.ContributionRepository;
import com.intuit.funding.dao.ProjectRepository;
import com.intuit.funding.dao.UserRepository;
import com.intuit.funding.entities.Project;
import com.intuit.funding.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.Assert;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {


    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetUserDetails(){
        User user = new User();
        user.setName("TEST");
        when(userRepository.getUserByEmail("TEST")).thenReturn(user);
        Assert.hasText("TEST",userService.getUserDetails("TEST").getName());
    }

    @Test
    void testSaveUserDetails(){
        User user = new User();
        user.setName("TEST");
        when(userRepository.getUserByEmail("TEST")).thenReturn(user);
        userService.saveUser(user);
        verify(userRepository).save(user);
    }
}
