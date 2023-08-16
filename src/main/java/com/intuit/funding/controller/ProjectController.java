package com.intuit.funding.controller;

import com.intuit.funding.exception.CustomException;
import com.intuit.funding.exception.Message;
import com.intuit.funding.entities.Contribution;
import com.intuit.funding.entities.Project;
import com.intuit.funding.entities.User;
import com.intuit.funding.service.ProjectService;
import com.intuit.funding.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void userDetails(Model model, Principal principal){
        if(principal != null){
            model.addAttribute("user",userService.getUserDetails(principal.getName()));
        }

    }

    // Display all project (public)
    @GetMapping("/project")
    public String getAllProject(Model model){
        model.addAttribute("title", "View Project");
        model.addAttribute("projects", projectService.getAllProject());
        return "project_list";
    }


    // Display specific project (public)
    @GetMapping("/project/{id}")
    public String getProjectDetail(@PathVariable("id") Long id, Model model) throws CustomException {
        try {
            model.addAttribute("projectDetail", projectService.getProject(id));
            model.addAttribute("contribution", new Contribution());
        }catch (Exception e){
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
        return "project_detail";
    }


    // View  project page by page.
    @GetMapping("/user/project/{page}")
    public String viewProject(@PathVariable("page") Integer page, Model model, Principal principal){
        Page<Project> projects = projectService.listProjectByPage(page, principal);
        model.addAttribute("projects",projects);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages", projects.getTotalPages());
        return "normal/view_project";
    }


    // View  project details by id.
    @GetMapping("/user/project/detail/{id}")
    public String showProjectDetails(@PathVariable("id") Long id, Model model, Principal principal) throws CustomException {
        System.out.println("ID = "+id);
        try {
            Project project = projectService.getProject(id);
            User user = userService.getUserDetails(principal.getName());

            if(project.getUser().getId() == user.getId()){
                model.addAttribute("projectDetail", project);
                model.addAttribute("contribution", new Contribution());
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
        return "normal/project_detail";
    }


    // Display the add project form to user.
    @GetMapping("/user/project")
    public  String addProjectForm(Model model, Principal principal){
        model.addAttribute("title","Add Project");
        model.addAttribute("project", new Project());
        return "normal/add_project";
    }


    // Add new project to db.
    @PostMapping("/user/project")
    public  String addProject(@ModelAttribute Project project, Principal principal, HttpSession session) throws CustomException {

        try{
            User user = userService.getUserDetails(principal.getName());

            project.setStatus("ACTIVE");
            project.setCurrentAmount(0.0);
            project.setCreatedDate(new Date());
            project.setUser(user);

            user.getProjects().add(project);

            userService.saveUser(user);

            session.setAttribute("message", new Message("Project added successfully","alert-success"));
        }catch (Exception e){
            session.setAttribute("message", new Message(e.getMessage() ,"alert-danger"));
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
        return "normal/add_project";
    }


    // delete a project
    @DeleteMapping("/user/delete/{id}")
    public String deleteProject(@PathVariable("id") Long id, Principal principal, HttpSession session) throws CustomException {
        try {
            Project project = projectService.getProject(id);
            User user = userService.getUserDetails(principal.getName());

            if(user.getId() == project.getUser().getId()){
                projectService.deleteProject(project);
                session.setAttribute("message", new Message("Successfully Deleted","alert-success"));
            }else {
                session.setAttribute("message", new Message("Project not found.","alert-danger"));
            }
        }catch (Exception e){
            session.setAttribute("message", new Message("Error occur while deleting","alert-danger"));
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
        return "redirect:/user/project/0";
    }


    // display update project form
    @GetMapping("/user/project/{id}/details")
    public String updateProject(@PathVariable("id") Long id, Model model, Principal principal) throws CustomException {
        model.addAttribute("title","Update Project");
        try {
            User user = userService.getUserDetails(principal.getName());
            Project project = projectService.getProject(id);

            if(user.getId() == project.getUser().getId()){
                model.addAttribute("project",project);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }

        return "normal/update_project";
    }

    // update project
    @PostMapping("/user/project/{id}/details")
    public String updateProjectDetails(@ModelAttribute Project project, HttpSession session, @PathVariable("id") Long id) throws CustomException {
        try {
            Project projectPrev = projectService.getProject(id);

            project.setUser(projectPrev.getUser());
            project.setEndDate(projectPrev.getEndDate());
            project.setStatus(projectPrev.getStatus());
            project.setCreatedDate(projectPrev.getCreatedDate());
            project.setCurrentAmount(projectPrev.getCurrentAmount());

            projectService.saveProject(project);
            session.setAttribute("message", new Message("Successfully Updated","alert-success"));
        }catch (Exception e){
            session.setAttribute("message", new Message("Error occur while updating","alert-danger"));
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
        return "normal/update_project";
    }

    //view all for user project
    @GetMapping("/user/project/all")
    public String viewAllProject(Model model){

        List<Project> projects = projectService.getAllProject();
        model.addAttribute("title", "View Project");
        model.addAttribute("projects",projects);
        return "normal/project_list";
    }
}
