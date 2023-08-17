package com.intuit.funding.controller;

import static org.mockito.Mockito.*;

import com.intuit.funding.controller.HomeController;
import com.intuit.funding.dao.UserRepository;
import com.intuit.funding.entities.User;
import com.intuit.funding.exception.CustomException;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

class HomeControllerTest {

    @InjectMocks
    private HomeController homeController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private HttpSession httpSession;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_ValidRegistration() throws CustomException {
        // Arrange
        User user = new User();
        user.setName("testuser");
        user.setPassword("testpassword");
        user.setEmail("test@example.com");

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);


        String viewName = homeController.register(user, true, model, httpSession);


        verify(httpSession, times(1)).setAttribute(anyString(), any());
        verify(model, times(1)).addAttribute(anyString(), any());

    }

    @Test
    void testHome() {
        String viewName = homeController.home(model);
        verify(model, times(1)).addAttribute("title", "Home-CrowdFunding");
    }

    @Test
    void testAbout() {
        String viewName = homeController.about(model);
        verify(model, times(1)).addAttribute("title", "About-CrowdFunding");
    }

    @Test
    void testSignup() {
        String viewName = homeController.signup(model);
        verify(model, times(1)).addAttribute("title", "Signup-CrowdFunding");
        verify(model, times(1)).addAttribute(eq("user"), any());
    }

    @Test
    void testLogin() {
        String viewName = homeController.login(model);
        verify(model, times(1)).addAttribute("title", "Login-CrowdFunding");
    }

}
