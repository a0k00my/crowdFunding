package com.intuit.funding.service;

import com.intuit.funding.dao.ProjectRepository;
import com.intuit.funding.dao.UserRepository;
import com.intuit.funding.entities.Project;
import com.intuit.funding.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    //TEST
    public List<Project> getAllProject() {
        return projectRepository.findAll();
    }

    //TEST
    public Project getProject(Long id) {
        return projectRepository.findById(id).get();
    }

    public Page<Project> listProjectByPage(Integer page, Principal principal){
        String email = principal.getName();
        User user = userRepository.getUserByEmail(email);
        // curent page + perpage
        Pageable pageable = PageRequest.of(page, 3);
        return projectRepository.findProjectByUser(user.getId(),pageable);
    }

    //TEST
    public void deleteProject(Project project) {
        projectRepository.delete(project);
    }

    //TEST
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }
}
