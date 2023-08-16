package com.intuit.funding.service;

import com.intuit.funding.dao.ContributionRepository;
import com.intuit.funding.dao.ProjectRepository;
import com.intuit.funding.dao.UserRepository;
import com.intuit.funding.entities.Project;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProjectServiceTest {

    @MockBean
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ContributionRepository contributionRepository;

    @InjectMocks
    private ContributionService contributionService;

    @Autowired
    ProjectService projectService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getProjectTest(){
        Project project = new Project();
        project.setTitle("Title");
        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.of(project));
        Assert.hasText("Title",projectService.getProject(1L).getTitle());
    }

    @Test
    public void getAllProjectTest(){
        List<Project> projectList = new ArrayList<>();
        projectList.add(new Project());
        when(projectRepository.findAll()).thenReturn(projectList);
        Assert.notEmpty(projectService.getAllProject(),"project list is empty");
    }

    @Test
    public void deleteProjectTest(){
        Project project = new Project();
        project.setTitle("Title");
        projectService.deleteProject(project);
        verify(projectRepository).delete(project);
    }

    @Test
    public void saveProjectTest(){
        Project project = new Project();
        project.setTitle("Title");
        projectService.saveProject(project);
        verify(projectRepository).save(project);
    }
}
