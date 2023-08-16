package com.intuit.funding.service;

import com.intuit.funding.dao.ContributionRepository;
import com.intuit.funding.dao.ProjectRepository;
import com.intuit.funding.dao.UserRepository;
import com.intuit.funding.entities.Contribution;
import com.intuit.funding.entities.Project;
import com.intuit.funding.entities.User;
import com.intuit.funding.service.ContributionService;
import com.intuit.funding.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.Assert;

import java.security.Principal;

import static org.mockito.Mockito.*;

@SpringBootTest
public class ContributionServiceTest {

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
    public void testUpdateProjectDetails() {
        Long projectId = 1L;
        double contributionAmount = 700.0;
        double requestedAmount = 500.0;
        double currentAmount = 300.0;
        String email = "test@example.com";

        Project project = new Project();
        project.setId(projectId);
        project.setRequestedAmount(requestedAmount);
        project.setCurrentAmount(currentAmount);

        User user = new User();
        user.setEmail(email);

        Contribution contribution = new Contribution();
        contribution.setAmount(contributionAmount);

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(email);

        project.setUser(user);

        when(projectRepository.findById(projectId)).thenReturn(java.util.Optional.of(project));
        when(userRepository.getUserByEmail(email)).thenReturn(user);
        when(contributionRepository.save(contribution)).thenReturn(contribution);
        when(projectRepository.save(project)).thenReturn(project);

        contributionService.updateProjectDetails(projectId, contribution, principal);
        verify(contributionRepository).save(contribution);
    }
}