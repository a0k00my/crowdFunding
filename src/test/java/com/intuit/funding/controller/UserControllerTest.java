package com.intuit.funding.controller;

import com.intuit.funding.service.UserService;
import com.intuit.funding.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDashboard() {
        // Mock objects
        User mockUser = new User();
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("username");
        when(userService.getUserDetails("username")).thenReturn(mockUser);
        String viewName = userController.dashboard(model);
        assertEquals("normal/home", viewName);
    }
}
