package com.intuit.funding.controller;

import com.intuit.funding.exception.CustomException;
import com.intuit.funding.exception.Message;
import com.intuit.funding.dao.UserRepository;
import com.intuit.funding.entities.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("title","Home-CrowdFunding");
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model){
        model.addAttribute("title","About-CrowdFunding");
        return "about";
    }

    @RequestMapping("/signup")
    public String signup(Model model){
        model.addAttribute("title","Signup-CrowdFunding");
        model.addAttribute("user", new User());
        return "signup";
    }

    // handler for registering user
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid @ModelAttribute("user") User user,
                           @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
                           Model model,
                           HttpSession session) throws CustomException {
        try {
            if(!agreement){
                System.out.println("Not agreed to terms and condition");
                throw new Exception("Not agreed to terms and condition");
            }

            user.setEnabled(true);
            user.setRole("ROLE_USER");
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(user);
            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("Successfully registered, please login to continue","alert-success"));
        }catch (Exception e){

            session.setAttribute("message", new Message(e.getMessage() ,"alert-danger"));
            throw new CustomException(e.getMessage());
        }

        return "signup";
    }

    // handler for login user
    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("title","Login-CrowdFunding");
        return "login";
    }

}
