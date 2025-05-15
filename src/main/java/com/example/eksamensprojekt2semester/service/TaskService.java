package com.example.eksamensprojekt2semester.service;



import com.example.eksamensprojekt2semester.exception.SubprojectNotFoundException;
import com.example.eksamensprojekt2semester.model.StateStatus;
import com.example.eksamensprojekt2semester.model.Task;
import com.example.eksamensprojekt2semester.model.TeamMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.eksamensprojekt2semester.repository.SubprojectRepository;
import com.example.eksamensprojekt2semester.repository.TaskAssignmentRepository;
import com.example.eksamensprojekt2semester.repository.TaskRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskAssignmentRepository taskAssignmentRepo;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private SubprojectRepository subprojectRepo;

    public void assignMemberToTask(int taskId, int memberId) {
        taskAssignmentRepo.assignMember(taskId, memberId);
    }

    public void unassignMemberFromTask(int taskId, int memberId) {
        taskAssignmentRepo.unassignMember(taskId, memberId);
    }

    public List<TeamMember> getAssignedMembers(int taskId) {
        return taskAssignmentRepo.getAssignedMembers(taskId);
    }

    public Task getTaskById(int id) {
        return taskRepo.getTaskById(id);
    }


    public boolean existsById(int taskId) {
        return taskRepo.existsById(taskId);
    }

    public void createTask(Task task) {
        /** Check if parent subproject exists **/
        if (!subprojectRepo.existsById(task.getSubprojectId())) {
            throw new SubprojectNotFoundException("Subproject not found with ID: " + task.getSubprojectId());
        }
        taskRepo.save(task);
    }

    public void updateTask(Task task) {
        /** Ensure the task exists **/
        if (!taskRepo.existsById(task.getId())) {
            throw new SubprojectNotFoundException("Task not found with ID: " + task.getId());
        }

        /** Check if parent subproject exists **/
        if (!subprojectRepo.existsById(task.getSubprojectId())) {
            throw new SubprojectNotFoundException("Subproject not found with ID: " + task.getSubprojectId());
        }

        taskRepo.update(task);
    }

    public void deleteTask(int id) {
        /** First remove any team member assignments,to avoid assignments referring to a task that no longer exists **/
        taskAssignmentRepo.removeAllAssignmentsForTask(id);

        /** Then delete the task **/
        taskRepo.deleteById(id);
    }

    /** Get all tasks **/
    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    /** Get all tasks for a specific subproject **/
    public List<Task> getTasksBySubprojectId(int subprojectId) {
        return taskRepo.findBySubprojectId(subprojectId);
    }

    public  BigDecimal calculateTotalHoursBySubproject(int subprojectId) {
        // Beregner det samlede antal estimerede timer for alle opgaver i et subprojekt
        return taskRepo.findBySubprojectId(subprojectId).stream()
                // Brug estimerede timer eller 0 hvis null
                .map(task -> task.getEstimatedHours() != null ? task.getEstimatedHours() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Læg alle timer sammen og returnér summen
}
    public  BigDecimal calculateRemainingHoursBySubproject(int subprojectId) {
        // Beregner hvor mange timer der samlet set er tilbage i et subprojekt
        return taskRepo.findBySubprojectId(subprojectId).stream()// Hent alle opgaver for subprojektet som en stream
                .map(task -> {
                    BigDecimal estimated = task.getEstimatedHours()
                            != null ? task.getEstimatedHours()
                            : BigDecimal.ZERO;
                    BigDecimal used = task.getUsedHours() != null ? task.getUsedHours() : BigDecimal.ZERO; // Brug brugte timer eller 0 hvis null
                    return estimated.subtract(used); // Beregn tilbageværende timer for opgaven
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Læg alle tilbageværende timer sammen og returnér summen
    }

    /**  Method to calculate the completion percentage of a subproject **/
    public int calculateProjectCompletion(int subprojectId) {
        /** Find all tasks for the specified subproject **/
        List<Task> tasks = taskRepo.findBySubprojectId(subprojectId);

        /** If there are no tasks, the completion percentage is 0% **/
        if (tasks.isEmpty()) {
            return 0;
        }

        /** Count how many tasks have status COMPLETED **/
        int completedTasks = 0;
        for (Task task : tasks) {

            /** Check if the task's status is COMPLETED **/
            if (task.getStatus() == StateStatus.COMPLETED) {
                completedTasks++;
            }
        }

        /** Calculate the percentage of completed tasks and round to the nearest whole number.
         *  The (double) is used to ensure precision in the division.
         *  Math.round() rounds the result to the nearest whole number **/
        return (int) Math.round((double) completedTasks / tasks.size() * 100);
    }

    /** Method to get the number of completed tasks for a subproject **/
    public int getCompletedTaskCount(int subprojectId) {
        return taskRepo.countBySubProjectIdAndStatus(subprojectId, StateStatus.COMPLETED);
    }

    /** Method to get the total number of tasks for a subproject **/
    public int getTotalTaskCount(int subprojectId) {
        return taskRepo.countBySubProjectId(subprojectId);
    }
}