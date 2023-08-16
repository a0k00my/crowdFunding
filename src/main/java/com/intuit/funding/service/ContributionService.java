package com.intuit.funding.service;

import com.intuit.funding.dao.ContributionRepository;
import com.intuit.funding.dao.ProjectRepository;
import com.intuit.funding.dao.UserRepository;
import com.intuit.funding.entities.Contribution;
import com.intuit.funding.entities.Project;
import com.intuit.funding.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class ContributionService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContributionRepository contributionRepository;

    //TEST
    public void updateProjectDetails(Long projectId, Contribution contribution, Principal principal) {
        Project project = projectRepository.findById(projectId).get();

        if(project.getStatus()=="ARCHIVED"){
            return;
        }

        String email = principal.getName();
        User user = userRepository.getUserByEmail(email);

        if (project.getTotalContribution() + contribution.getAmount() > project.getRequestedAmount()) {
            contribution.setAmount(project.getRequestedAmount() - project.getTotalContribution());
            project.setStatus("ARCHIVED");
            project.setEndDate(new Date());

            User currProjectUser = project.getUser();
            currProjectUser.setAccountBalance(user.getAccountBalance() + project.getRequestedAmount());

            List<Contribution> contributions = project.getContributions();
            for( Contribution c : contributions){
                c.setTransferred(true);
            }

            project.setContributions(contributions);

            userRepository.save(currProjectUser);
            projectRepository.save(project);

            contribution.setTransferred(true);
        }else {
            contribution.setTransferred(false);
        }
        contribution.setProject(project);
        contribution.setUser(user);
        contribution.setCreatedDate(new Date());
        contributionRepository.save(contribution);
    }

    public List<Contribution> getContribution(String email) {
        User user = userRepository.getUserByEmail(email);
        List<Contribution> contribution = contributionRepository.getContribution(user.getId());
        return contribution;
    }
}
