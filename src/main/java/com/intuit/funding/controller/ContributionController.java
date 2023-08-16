package com.intuit.funding.controller;

import com.intuit.funding.entities.Project;
import com.intuit.funding.exception.CustomException;
import com.intuit.funding.exception.Message;
import com.intuit.funding.entities.Contribution;
import com.intuit.funding.service.ContributionService;
import com.intuit.funding.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;


@Controller
public class ContributionController {

    @Autowired
    private UserService userService;

    @Autowired
    private ContributionService contributionService;

    @ModelAttribute
    public void userDetails(Model model, Principal principal){
        model.addAttribute("user",userService.getUserDetails(principal.getName()));
    }

    @PostMapping("/user/contribution/project/{id}")
    public String updateProjectDetails(@ModelAttribute Contribution contribution,
                                       HttpSession session, @PathVariable("id") Long id,
                                       Principal principal, Model model) throws CustomException {
        try {
            contributionService.updateProjectDetails(id,contribution,principal);
            session.setAttribute("message", new Message(contribution.getAmount()+ " Contribution saved successfully","alert-success"));
        }catch (Exception e){
            session.setAttribute("message", new Message("Error occur while saving contribution","alert-danger"));
            throw new CustomException(e.getMessage());
        }
        return "redirect:/project/"+id;
    }

    @GetMapping("/user/contribution/all")
    public String viewContribution(Principal principal, Model model){
        List<Contribution> contribution = contributionService.getContribution(principal.getName());
        model.addAttribute("contribution", contribution);
        return "normal/view_contribution";
    }
}
