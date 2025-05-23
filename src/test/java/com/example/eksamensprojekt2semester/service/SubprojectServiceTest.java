package com.example.eksamensprojekt2semester.service;

import com.example.eksamensprojekt2semester.model.Subproject;
import com.example.eksamensprojekt2semester.repository.SubprojectRepository;
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

    @Test
    void createSubproject_successful() {
        Subproject subproject = new Subproject();
        subproject.setName("Test Subproject");
        subproject.setProjectId(1);
        subproject.setCompletionPercentage(50);

        when(projectService.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> subprojectService.createSubproject(subproject));
        verify(subprojectRepository).save(subproject);
    }

    @Test
    void createSubproject_nameIsNull_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
                subprojectService.createSubproject(new Subproject(null,1)));
    }

    @Test
    void createSubproject_projectIdIsZero_shouldThrow() {
        Subproject subproject = new Subproject();
        assertThrows(IllegalArgumentException.class, () ->
                subprojectService.createSubproject(new Subproject("Valid Name", 0)));
    }

    @Test
    void createSubproject_projectNotFound_shouldThrow() {
        Subproject subproject = new Subproject();
        subproject.setName("Valid Name");
        subproject.setProjectId(999); // Non-existent project
        subproject.setCompletionPercentage(50);

        when(projectService.existsById(999)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                subprojectService.createSubproject(subproject));
    }
}
