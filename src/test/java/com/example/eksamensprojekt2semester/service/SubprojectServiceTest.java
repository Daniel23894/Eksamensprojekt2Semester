package com.example.eksamensprojekt2semester.service;

import org.junit.jupiter.api.Test;
import com.example.eksamensprojekt2semester.model.Subproject;
import com.example.eksamensprojekt2semester.repository.SubprojectRepository;
import com.example.eksamensprojekt2semester.service.ProjectService;
import com.example.eksamensprojekt2semester.service.SubprojectService;
import com.example.eksamensprojekt2semester.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SubprojectServiceTest {

    @Mock
    private SubprojectRepository subprojectRepository;

    @Mock
    private ProjectService projectService;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private SubprojectService subprojectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //1. Test: Successful Subproject Creation
    @Test
    void createSubproject_successful() {
        Subproject subproject = new Subproject();
        subproject.setName("Test Subproject");
        subproject.setProjectId(1);
        subproject.setCompletionPercentage(50);

        when(projectService.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> subprojectService.createSubproject(subproject));
        verify(subprojectRepository, times(1)).save(subproject);
    }

    //2. Test: Name is null
    @Test
    void createSubproject_nameIsNull_throwsException() {
        Subproject subproject = new Subproject();
        subproject.setName(null);
        subproject.setProjectId(1);
        subproject.setCompletionPercentage(50);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                subprojectService.createSubproject(subproject));
        assertEquals("Subprojekt navn er påkrævet", exception.getMessage());
    }

    //3. Test: Project ID is 0
    @Test
    void createSubproject_invalidProjectId_throwsException() {
        Subproject subproject = new Subproject();
        subproject.setName("Valid Name");
        subproject.setProjectId(0);
        subproject.setCompletionPercentage(50);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                subprojectService.createSubproject(subproject));
        assertEquals("Valg af overordnet projekt er påkrævet", exception.getMessage());
    }

    //3. Test: Project ID is 0
    @Test
    void createSubproject_projectNotFound_throwsException() {
        Subproject subproject = new Subproject();
        subproject.setName("Valid Name");
        subproject.setProjectId(999);
        subproject.setCompletionPercentage(50);

        when(projectService.existsById(999)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                subprojectService.createSubproject(subproject));
        assertEquals("Det valgte projekt findes ikke", exception.getMessage());
    }
}