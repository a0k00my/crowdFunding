package com.intuit.funding.controller;


import com.intuit.funding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void userDetails(Model model, Principal principal){
        model.addAttribute("user",userService.getUserDetails(principal.getName()));
    }

    @RequestMapping("/home")
    public String dashboard(Model model){
        return "normal/home";
    }












}
