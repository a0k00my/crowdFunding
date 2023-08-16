package com.intuit.funding.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.intuit.funding.controller.ProjectController;
import com.intuit.funding.dao.ProjectRepository;
import com.intuit.funding.dao.UserRepository;
import com.intuit.funding.entities.Contribution;
import com.intuit.funding.entities.Project;
import com.intuit.funding.entities.User;
import com.intuit.funding.exception.CustomException;
import com.intuit.funding.exception.Message;
import com.intuit.funding.service.ProjectService;
import com.intuit.funding.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    private MockMvc mockMvc;

    @Mock
    private Principal principal;

    @Mock
    private UserService userService;

    @Mock
    private Page<Project> projectPage;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
    }

    @Test
    public void testGetAllProject() throws Exception {
        List<Project> projects = new ArrayList<>();
        projects.add(new Project());
        projects.add(new Project());

        when(projectService.getAllProject()).thenReturn(projects);

        mockMvc.perform(get("/project"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("projects", projects))
                .andExpect(view().name("project_list"));

        verify(projectService).getAllProject();
    }

    @Test
    public void testGetProjectDetail() throws Exception {
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);

        when(projectService.getProject(projectId)).thenReturn(project);

        mockMvc.perform(get("/project/{id}", projectId))
                .andExpect(status().isOk())
                .andExpect(model().attribute("projectDetail", project))
                .andExpect(view().name("project_detail"));

        verify(projectService).getProject(projectId);
    }

    @Test
    public void testViewProject() {
        Integer page = 0;
        User mockUser = mock(User.class);
        when(principal.getName()).thenReturn("username");
        when(userService.getUserDetails("username")).thenReturn(mockUser);
        when(projectService.listProjectByPage(page, principal)).thenReturn(projectPage);

        String viewName = projectController.viewProject(page, model, principal);

        verify(model).addAttribute("projects", projectPage);
    }

    @Test
    public void testShowProjectDetails() throws CustomException {
        Long projectId = 1L;
        User mockUser = mock(User.class);
        Project mockProject = mock(Project.class);
        when(principal.getName()).thenReturn("username");
        when(userService.getUserDetails("username")).thenReturn(mockUser);
        when(projectService.getProject(projectId)).thenReturn(mockProject);
        when(mockUser.getId()).thenReturn(1L);
        when(mockProject.getUser()).thenReturn(mockUser);

        String viewName = projectController.showProjectDetails(projectId, model, principal);

        verify(model).addAttribute("projectDetail", mockProject);
    }

    @Test
    public void testAddProjectForm() {
        String viewName = projectController.addProjectForm(model, principal);

        verify(model).addAttribute("title", "Add Project");
    }

    @Test
    public void testAddProject() throws CustomException {
        User mockUser = mock(User.class);
        Project mockProject = new Project();
        when(principal.getName()).thenReturn("username");
        when(userService.getUserDetails("username")).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(1L);
        when(userService.saveUser(mockUser)).thenReturn(mockUser);

        String viewName = projectController.addProject(mockProject, principal, session);

        verify(session).setAttribute(eq("message"), any(Message.class));
    }

    @Test
    public void testDeleteProject() throws CustomException {
        User mockUser = mock(User.class);
        Project mockProject = new Project();
        mockProject.setUser(mockUser);
        when(principal.getName()).thenReturn("username");
        when(userService.getUserDetails("username")).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(1L);
        when(projectService.getProject(1L)).thenReturn(mockProject);

        String viewName = projectController.deleteProject(1L, principal, session);

        verify(projectService).deleteProject(mockProject);
        verify(session).setAttribute(eq("message"), any(Message.class));
    }

    @Test
    public void testUpdateProject() throws CustomException {

        User mockUser = mock(User.class);
        Project mockProject = new Project();
        mockProject.setUser(mockUser);

        when(principal.getName()).thenReturn("username");
        when(userService.getUserDetails("username")).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(1L);
        when(projectService.getProject(1L)).thenReturn(mockProject);


        Project updatedProject = new Project();
        updatedProject.setId(1L);

        String viewName = projectController.updateProject(1L, model, principal);
        assertEquals("normal/update_project", viewName);
    }


    @Test
    public void testUpdateProjectDetails() throws CustomException {
        // Mock objects
        User mockUser = mock(User.class);
        Project mockProject = new Project();
        mockProject.setUser(mockUser);
        when(principal.getName()).thenReturn("username");
        when(userService.getUserDetails("username")).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(1L);
        when(projectService.getProject(1L)).thenReturn(mockProject);
        Project updatedProject = new Project();
        updatedProject.setId(1L); // Set the same ID as the mock project
        updatedProject.setTitle("Updated Project");
        updatedProject.setDescription("Updated description");
        updatedProject.setEndDate(new Date());
        String viewName = projectController.updateProjectDetails(updatedProject, session, 1L);

        verify(projectService).getProject(1L);
        verify(projectService).saveProject(updatedProject);
        verify(session).setAttribute(eq("message"), any(Message.class));
        assertEquals("normal/update_project", viewName);
    }

    @Test
    public void testViewAllProject() {
        List<Project> mockProjects = new ArrayList<>();
        Project mockProject1 = new Project();
        Project mockProject2 = new Project();
        mockProjects.add(mockProject1);
        mockProjects.add(mockProject2);
        when(projectService.getAllProject()).thenReturn(mockProjects);
        String viewName = projectController.viewAllProject(model);
        verify(projectService).getAllProject();
        verify(model).addAttribute(eq("title"), eq("View Project"));
        verify(model).addAttribute(eq("projects"), eq(mockProjects));
        assertEquals("normal/project_list", viewName);
    }

}
