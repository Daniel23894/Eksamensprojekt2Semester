package com.example.eksamensprojekt2semester.service;

import com.example.eksamensprojekt2semester.exception.SubprojectNotFoundException;
import com.example.eksamensprojekt2semester.model.StateStatus;
import com.example.eksamensprojekt2semester.model.Subproject;
import com.example.eksamensprojekt2semester.model.Task;
import com.example.eksamensprojekt2semester.model.TeamMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.eksamensprojekt2semester.repository.SubprojectRepository;
import com.example.eksamensprojekt2semester.repository.TaskAssignmentRepository;
import com.example.eksamensprojekt2semester.repository.TaskRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskAssignmentRepository taskAssignmentRepo;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private SubprojectRepository subprojectRepo;

    @Transactional
    public void assignMemberToTask(int taskId, int memberId) {
        taskAssignmentRepo.assignMember(taskId, memberId);
    }

    @Transactional
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

    @Transactional
    public int createTask(Task task) {
        /** Check if parent subproject exists **/
        if (!subprojectRepo.existsById(task.getSubprojectId())) {
            throw new SubprojectNotFoundException("Subproject not found with ID: " + task.getSubprojectId());
        }

        /** Set default value to avoid NullPointerException **/
        if (task.getCompletionPercentage() == null) {
            task.setCompletionPercentage(0);
        }

        return taskRepo.save(task);
    }

    @Transactional
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

    @Transactional
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

    public int calculateProjectCompletion(int projectId) {
        /** Get all subprojects for the given projectId **/
        List<Subproject> subprojects = subprojectRepo.findByProjectId(projectId);

        int totalTasks = 0;
        int completedTasks = 0;

        /** Loop over each subproject **/
        for (Subproject subproject : subprojects) {
            List<Task> tasks = taskRepo.findBySubprojectId(subproject.getId());  /** Find tasks for the subproject **/

            /** Count tasks and completed tasks **/
            for (Task task : tasks) {
                totalTasks++;
                if (task.getStatus() == StateStatus.COMPLETED) {
                    completedTasks++;
                }
            }
        }

        if (totalTasks == 0) {
            return 0; /** means 0% complete **/
        }
        return (int) Math.round((double) completedTasks / totalTasks * 100);
    }

    /**  Method to calculate the completion percentage of a subproject **/
    public int calculateSubProjectCompletion(int subprojectId) {
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



    /**  Method to calculate the completion percentage of a subproject **/
    public void calculateTaskCompletion(int taskId) {
        /** Find the specific tasks for the specified task id **/
         Optional<Task> optionalTask = taskRepo.findById(taskId);

        /** If there is no task, the completion percentage is 0% **/
        if (optionalTask.isEmpty()) {
            return; /** No task found, exit the method **/
        }

        Task task = optionalTask.get();

        StateStatus status = task.getStatus();
        int completionPercentage = 0;

        switch (status) {
            case COMPLETED:
                completionPercentage = 100;
                break;
            case IN_PROGRESS:
                completionPercentage = 50; // or your own logic
                break;
            case NOT_STARTED:
            case PAUSED:
            case CANCELLED:
            default:
                completionPercentage = 0;
                break;
        }

        task.setCompletionPercentage(completionPercentage);
        taskRepo.update(task);  // <--- SAVE the changes

        System.out.println("Status: " + status + ", Completion: " + completionPercentage);
    }


    /** Method to get the number of completed tasks for a subproject **/
    public int getCompletedTaskCount(int subprojectId) {
        return taskRepo.countBySubprojectIdAndStatus(subprojectId, StateStatus.COMPLETED);
    }

    /** Method to get the total number of tasks for a subproject **/
    public int getTotalTaskCount(int subprojectId) {
        return taskRepo.countBySubprojectId(subprojectId);
    }
}