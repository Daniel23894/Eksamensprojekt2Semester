package com.example.eksamensprojekt2semester.service;

import com.example.eksamensprojekt2semester.model.StateStatus;
import com.example.eksamensprojekt2semester.model.Subproject;
import com.example.eksamensprojekt2semester.model.Task;
import com.example.eksamensprojekt2semester.repository.SubprojectRepository;
import com.example.eksamensprojekt2semester.repository.TaskRepository;
import com.example.eksamensprojekt2semester.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private SubprojectRepository subprojectRepo;

    @Mock
    private TaskRepository taskRepo;

    @InjectMocks
    private TaskService taskService;  // Assuming this method belongs here

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void calculateProjectCompletion_success() {
        int projectId = 1;

        Subproject sp1 = new Subproject();
        sp1.setId(10);
        Subproject sp2 = new Subproject();
        sp2.setId(20);

        when(subprojectRepo.findByProjectId(projectId)).thenReturn(List.of(sp1, sp2));

        Task t1 = new Task(); t1.setStatus(StateStatus.COMPLETED);
        Task t2 = new Task(); t2.setStatus(StateStatus.PAUSED);
        Task t3 = new Task(); t3.setStatus(StateStatus.COMPLETED);

        when(taskRepo.findBySubprojectId(10)).thenReturn(List.of(t1, t2));
        when(taskRepo.findBySubprojectId(20)).thenReturn(List.of(t3));

        int result = taskService.calculateProjectCompletion(projectId);

        // totalTasks = 3, completedTasks = 2 => 66%
        assertEquals(67, result);
    }

    @Test
    void calculateProjectCompletion_noSubprojectsOrTasks_returnsZero() {
        int projectId = 2;

        when(subprojectRepo.findByProjectId(projectId)).thenReturn(List.of()); // no subprojects

        int result = taskService.calculateProjectCompletion(projectId);

        assertEquals(0, result);
    }
}


