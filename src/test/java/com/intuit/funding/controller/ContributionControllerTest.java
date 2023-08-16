package com.intuit.funding.controller;

import com.intuit.funding.entities.Contribution;
import com.intuit.funding.exception.CustomException;
import com.intuit.funding.exception.Message;
import com.intuit.funding.service.ContributionService;
import com.intuit.funding.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ContributionControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ContributionService contributionService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private ContributionController contributionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateProjectDetails() throws CustomException {
        Principal principal = mock(Principal.class);
        Contribution contribution = new Contribution();
        Long projectId = 1L;
        when(principal.getName()).thenReturn("username");

        String viewName = contributionController.updateProjectDetails(contribution, session, projectId, principal, model);

        verify(session).setAttribute(eq("message"), any(Message.class));
        verify(contributionService).updateProjectDetails(eq(projectId), eq(contribution), eq(principal));
    }

    @Test
    void testViewContribution() {
        Principal principal = mock(Principal.class);
        List<Contribution> contributions = Collections.singletonList(new Contribution());
        when(contributionService.getContribution("username")).thenReturn(contributions);

        String viewName = contributionController.viewContribution(principal, model);

        assertEquals("normal/view_contribution", viewName);
    }
}
