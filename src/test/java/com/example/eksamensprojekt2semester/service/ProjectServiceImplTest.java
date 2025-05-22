package com.example.eksamensprojekt2semester.service;

import com.example.eksamensprojekt2semester.exception.ProjectNotFoundException;
import com.example.eksamensprojekt2semester.model.Project;
import com.example.eksamensprojekt2semester.repository.ProjectRepository;
import com.example.eksamensprojekt2semester.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectServiceImpl projectServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void updateProject_successful() {
        Project project = new Project();
        project.setProjectId(1);
        project.setName("New Name");

        when(projectRepository.update(project)).thenReturn(1); // 1 row updated

        assertDoesNotThrow(() -> projectServiceImpl.updateProject(project));

        verify(projectRepository, times(1)).update(project); // check update was called once
    }

    @Test
    void deleteProjectById_shouldSucceed_whenProjectExists() {
        // Arrange
        int projectId = 1;
        ProjectRepository mockRepository = Mockito.mock(ProjectRepository.class);
        ProjectServiceImpl service = new ProjectServiceImpl(mockRepository);

        // Mock: 1 row affected = success
        Mockito.when(mockRepository.delete(projectId)).thenReturn(1);

        // Act + Assert (should not throw)
        assertDoesNotThrow(() -> service.deleteProjectById(projectId));

        // Verify that the repository method was called
        Mockito.verify(mockRepository).delete(projectId);
    }

    @Test
    void deleteProjectById_shouldThrowException_whenProjectDoesNotExist() {
        // Arrange
        int projectId = 99;
        ProjectRepository mockRepository = Mockito.mock(ProjectRepository.class);
        ProjectServiceImpl service = new ProjectServiceImpl(mockRepository);

        // Mock: 0 rows affected = project not found
        Mockito.when(mockRepository.delete(projectId)).thenReturn(0);

        // Act + Assert
        ProjectNotFoundException exception = assertThrows(
                ProjectNotFoundException.class,
                () -> service.deleteProjectById(projectId)
        );

        assertEquals("Project with ID 99 not found.", exception.getMessage());

        // Verify call
        Mockito.verify(mockRepository).delete(projectId);
    }
}
